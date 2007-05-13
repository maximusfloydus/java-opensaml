/*
 * Copyright [2006] [University Corporation for Advanced Internet Development, Inc.]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opensaml.saml2.encryption;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import javolution.util.FastList;

import org.apache.log4j.Logger;
import org.opensaml.Configuration;
import org.opensaml.common.IdentifierGenerator;
import org.opensaml.common.impl.SecureRandomIdentifierGenerator;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.BaseID;
import org.opensaml.saml2.core.EncryptedAssertion;
import org.opensaml.saml2.core.EncryptedAttribute;
import org.opensaml.saml2.core.EncryptedElementType;
import org.opensaml.saml2.core.EncryptedID;
import org.opensaml.saml2.core.NameID;
import org.opensaml.saml2.core.NewEncryptedID;
import org.opensaml.saml2.core.NewID;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.XMLObjectBuilderFactory;
import org.opensaml.xml.encryption.CarriedKeyName;
import org.opensaml.xml.encryption.DataReference;
import org.opensaml.xml.encryption.EncryptedData;
import org.opensaml.xml.encryption.EncryptedKey;
import org.opensaml.xml.encryption.EncryptionConstants;
import org.opensaml.xml.encryption.EncryptionException;
import org.opensaml.xml.encryption.EncryptionParameters;
import org.opensaml.xml.encryption.KeyEncryptionParameters;
import org.opensaml.xml.encryption.ReferenceList;
import org.opensaml.xml.io.Marshaller;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.signature.KeyInfo;
import org.opensaml.xml.signature.KeyName;
import org.opensaml.xml.signature.RetrievalMethod;
import org.opensaml.xml.util.DatatypeHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Class which implements SAML2-specific options for {@link org.opensaml.saml2.core.EncryptedElementType} objects.
 */
public class Encrypter {
    
    // TODO possibly support KEK input as list of recipient(s) + resolver(s) + algorithm(s) + ?
    // - need more key resolver work

    /**
     * Options for where to place the resulting EncryptedKey elements with respect
     * to the associated EncryptedData element.
     */
    public enum KeyPlacement {
        /** Place the EncryptedKey element(s) as a peer to the EncryptedData inside the EncryptedElementType. */
        PEER,
    
        /** Place the EncryptedKey element(s) within the KeyInfo of the EncryptedData. */
        INLINE
    }
    
    /** Factory for building XMLObject instances. */
    private XMLObjectBuilderFactory builderFactory;
    
    /** Generator for XML ID attribute values. */
    private IdentifierGenerator idGenerator;
    
    /** The parameters to use for encrypting the data. */
    private EncryptionParameters encParams;
    
    /** The parameters to use for encrypting (wrapping) the data encryption key. */
    private List<KeyEncryptionParameters> kekParams;
    
    /** The option for where to place the generated EncryptedKey elements. */
    private KeyPlacement keyPlacement;
    
    /** The XML encrypter instance to use. */
    private org.opensaml.xml.encryption.Encrypter xmlEncrypter;
    
    /** Specifies whether to reuse a generated data encryption key 
     * across multiple calls to a given Encrypter instance. */
    private boolean reuseDataEncryptionKey;
    
    /** Specifies whether an Encrypter instance can be reused. */
    private boolean reusable;

    /** Internal flag to track whether is first use or not. */
    private boolean firstUse;

    /** Class logger. */
    private Logger log = Logger.getLogger(Encrypter.class);

    
    /**
     * Constructor.
     *
     * @param dataEncParams the data encryption parameters
     * @param keyEncParams the key encryption parameters
     */
    public Encrypter(EncryptionParameters dataEncParams, List<KeyEncryptionParameters> keyEncParams) {
        this.encParams = dataEncParams;
        this.kekParams = keyEncParams;
        
        init();
    }
 
    /**
     * Constructor.
     *
     * @param dataEncParams the data encryption parameters
     * @param keyEncParam the key encryption parameter
     */
    public Encrypter(EncryptionParameters dataEncParams, KeyEncryptionParameters keyEncParam) {
        List<KeyEncryptionParameters> keks = new ArrayList<KeyEncryptionParameters>();
        keks.add(keyEncParam);
        
        this.encParams = dataEncParams;
        this.kekParams = keks;
        
        init();
    }
    
    /**
     * Constructor.
     *
     * @param dataEncParams the data encryption parameters
     */
    public Encrypter(EncryptionParameters dataEncParams) {
        List<KeyEncryptionParameters> keks = new ArrayList<KeyEncryptionParameters>();
        
        this.encParams = dataEncParams;
        this.kekParams = keks;
        
        init();
    }
    
    /**
     * Helper method for constructors.
     */
    private void init() {
        builderFactory = Configuration.getBuilderFactory();
        idGenerator = new SecureRandomIdentifierGenerator();
        xmlEncrypter = new org.opensaml.xml.encryption.Encrypter();
        
        keyPlacement = KeyPlacement.PEER;
        reuseDataEncryptionKey = false;
        
        firstUse = true;
        reusable = true;
        if (encParams.getKeyInfo() != null) {
            reusable = false;
        }
        for (KeyEncryptionParameters kekParam: kekParams) {
            if (kekParam.getKeyInfo() != null) {
                reusable = false;
                return;
            }
        }
    }
    
    /**
     * Set the generator to use when creating XML ID attribute values.
     * 
     * @param newIDGenerator the new IdentifierGenerator to use
     */
    public void setIDGenerator(IdentifierGenerator newIDGenerator) {
        this.idGenerator = newIDGenerator;
    }

    /**
     * Get the current key placement option.
     * 
     * @return returns the key placement option.
     */
    public KeyPlacement getKeyPlacement() {
        return this.keyPlacement;
    }

    /**
     * Set the key placement option.
     * 
     * @param newKeyPlacement The new key placement option to set
     */
    public void setKeyPlacement(KeyPlacement newKeyPlacement) {
        this.keyPlacement = newKeyPlacement;
    }

    /**
     * Get the flag determining data encryption key reuse.
     * 
     * @return Returns the data encryption key reuse option
     */
    public boolean reuseDataEncryptionKey() {
        return this.reuseDataEncryptionKey;
    }

    /**
     * Set the flag determining data encryption key reuse.
     * 
     * @param newReuseDataEncryptionKey The new reuseDataEncryptionKey option to set
     */
    public void setReuseDataEncryptionKey(boolean newReuseDataEncryptionKey) {
        this.reuseDataEncryptionKey = newReuseDataEncryptionKey;
    }
    
    /**
     * Check whether this Encrypter instance may be used to encrypt multiple objects.
     * 
     * @return true if Encrypter is reusable
     */
    public boolean isReusable() {
        return reusable;
    }

    /**
     * Encrypt the specified Assertion.
     * 
     * @param assertion the Assertion to encrypt
     * @return an EncryptedAssertion 
     * @throws EncryptionException thrown when encryption generates an error
     */
    public EncryptedAssertion encrypt(Assertion assertion) throws EncryptionException {
        return (EncryptedAssertion) encrypt(assertion, EncryptedAssertion.DEFAULT_ELEMENT_NAME);
    }

    /**
     * Encrypt the specified Assertion, treating as an identifier and returning
     * an EncryptedID.
     * 
     * @param assertion the Assertion to encrypt
     * @return an EncryptedID 
     * @throws EncryptionException thrown when encryption generates an error
     */
    public EncryptedID encryptAsID(Assertion assertion) throws EncryptionException {
        return (EncryptedID) encrypt(assertion, EncryptedID.DEFAULT_ELEMENT_NAME);
    }
    
    /**
     * Encrypt the specified Attribute.
     * 
     * @param attribute the Attribute to encrypt
     * @return an EncryptedAttribute
     * @throws EncryptionException thrown when encryption generates an error
     */
    public EncryptedAttribute encrypt(Attribute attribute) throws EncryptionException {
        return (EncryptedAttribute) encrypt(attribute, EncryptedAttribute.DEFAULT_ELEMENT_NAME);
    }

    /**
     * Encrypt the specified NameID.
     * 
     * @param nameID the NameID to encrypt
     * @return an EncryptedID
     * @throws EncryptionException thrown when encryption generates an error
     */
    public EncryptedID encrypt(NameID nameID) throws EncryptionException {
        return (EncryptedID) encrypt(nameID, EncryptedID.DEFAULT_ELEMENT_NAME);
    }

    /**
     * Encrypt the specified BaseID.
     * 
     * @param baseID the BaseID to encrypt
     * @return an EncryptedID
     * @throws EncryptionException thrown when encryption generates an error
     */
    public EncryptedID encrypt(BaseID baseID) throws EncryptionException {
        return (EncryptedID) encrypt(baseID, EncryptedID.DEFAULT_ELEMENT_NAME);
    }

    /**
     * Encrypt the specified NewID.
     * 
     * @param newID the NewID to encrypt
     * @return a NewEncryptedID
     * @throws EncryptionException thrown when encryption generates an error
     */
    public NewEncryptedID encrypt(NewID newID) throws EncryptionException {
        return (NewEncryptedID) encrypt(newID, NewEncryptedID.DEFAULT_ELEMENT_NAME);
    }
    
    /**
     * Encrypt the specified XMLObject, and return it as an instance of the specified QName,
     * which should be one of the types derived from {@link org.opensaml.saml2.core.EncryptedElementType}.
     * 
     * @param xmlObject the XMLObject to encrypt
     * @param encElementName the QName of the specialization of EncryptedElementType to return
     * @return a specialization of {@link org.opensaml.saml2.core.EncryptedElementType}
     * @throws EncryptionException thrown when encryption generates an error
     */
    private EncryptedElementType encrypt(XMLObject xmlObject, QName encElementName) throws EncryptionException {
        if (!isReusable() && !firstUse) {
            throw new EncryptionException("Encrypter instance has already been used and is not reusable");
        }
       
        EncryptedElementType encElement = 
            (EncryptedElementType) builderFactory.getBuilder(encElementName).buildObject(encElementName);
        
        // Marshall the containing element, we will need its Document context to pass 
        // to the key encryption method
        Marshaller marshaller = Configuration.getMarshallerFactory().getMarshaller(encElement);
        Element domElement;
        try {
            domElement = marshaller.marshall(encElement);
        } catch (MarshallingException e) {
            throw new EncryptionException("Error marshalling target XMLObject", e);
        }
        Document ownerDocument = domElement.getOwnerDocument();
        
        try {
            if (encParams.getEncryptionKey() == null) {
                encParams.setEncryptionKey(xmlEncrypter.generateEncryptionKey(encParams.getAlgorithm()));
            }
        } catch (EncryptionException e) {
            log.error("Encrypter could not generate the data encryption key: ", e);
            throw e;
        }
        
        EncryptedData encData = encryptElement(xmlObject);
        List<EncryptedKey> encKeys = encryptKeys(ownerDocument);
        
        if (!reuseDataEncryptionKey()) {
            encParams.setEncryptionKey(null);
        }
        
        firstUse = false;
        
        return processElements(encElement, encData, encKeys);
    }

    /**
     * Handle post-processing of generated EncryptedData and EncryptedKey(s) and storage in the appropriate
     * EncryptedElementType instance.
     * 
     * @param encElement the EncryptedElementType instance which will hold the encrypted data and keys
     * @param encData the EncryptedData object
     * @param encKeys the list of EncryptedKey objects
     * @return the processed EncryptedElementType instance
     * 
     * @throws EncryptionException thrown when processing encounters an error
     */
    protected EncryptedElementType processElements(EncryptedElementType encElement,
            EncryptedData encData, List<EncryptedKey> encKeys) throws EncryptionException {
        // First ensure certain elements/attributes are non-null, common to all cases.
        if (encData.getID() == null) {
            encData.setID(idGenerator.generateIdentifier());
        }
        
        // If not doing key wrapping, just return the encrypted element
        if (encKeys.isEmpty()) {
            encElement.setEncryptedData(encData);
            return encElement;
        }
        
        if (encData.getKeyInfo() == null) {
            KeyInfo dataKeyInfo = 
                (KeyInfo) builderFactory
                    .getBuilder(KeyInfo.DEFAULT_ELEMENT_NAME).buildObject(KeyInfo.DEFAULT_ELEMENT_NAME);
            encData.setKeyInfo(dataKeyInfo);
        }
        
        for (EncryptedKey encKey : encKeys) {
            if (encKey.getID() == null) {
                encKey.setID(idGenerator.generateIdentifier());
            }
        }
        
        switch (keyPlacement) {
            case INLINE:
                return placeKeysInline(encElement, encData, encKeys);
            case PEER:
                return placeKeysAsPeers(encElement, encData, encKeys);
            default:
                //Shouldn't be able to get here, but just in case...
                throw new EncryptionException("Unsupported key placement option was specified: " + keyPlacement);
        }
    }

    /**
     * Place the EncryptedKey elements inside the KeyInfo element within the EncryptedData element.
     * 
     * Although operationally trivial, this method is provided so that subclasses may 
     * override or augment as desired.
     * 
     * @param encElement the EncryptedElementType instance which will hold the encrypted data and keys
     * @param encData the EncryptedData object
     * @param encKeys the list of EncryptedKey objects
     * @return the processed EncryptedElementType instance
     */
    protected EncryptedElementType placeKeysInline(EncryptedElementType encElement,
            EncryptedData encData, List<EncryptedKey> encKeys) {
        
        encData.getKeyInfo().getEncryptedKeys().addAll(encKeys);
        encElement.setEncryptedData(encData);
        return encElement;
    }
    
    /**
     * Store the specified EncryptedData and EncryptedKey(s) in the specified instance of EncryptedElementType
     * as peer elements, following SAML 2 Errata E43 guidelines for forward and back referencing between the
     * EncryptedData and EncryptedKey(s).
     * 
     * @param encElement a specialization of EncryptedElementType to store the encrypted data and keys
     * @param encData the EncryptedData to store
     * @param encKeys the EncryptedKey(s) to store
     * @return the resulting specialization of EncryptedElementType
     */
    protected EncryptedElementType placeKeysAsPeers(EncryptedElementType encElement,
            EncryptedData encData, List<EncryptedKey> encKeys) {
        
        for (EncryptedKey encKey : encKeys) {
            if (encKey.getReferenceList() == null) {
                ReferenceList rl = 
                    (ReferenceList) builderFactory
                        .getBuilder(ReferenceList.DEFAULT_ELEMENT_NAME).buildObject(ReferenceList.DEFAULT_ELEMENT_NAME);
                encKey.setReferenceList(rl);
            }
        }
        
        // If there is only 1 EncryptedKey we have a simple forward reference (RetrievalMethod) 
        // and back reference (ReferenceList/DataReference) requirement.
        // Multiple "multicast" keys use back reference + CarriedKeyName
        if (encKeys.size() == 1) {
            linkSinglePeerKey(encData, encKeys.get(0));
        } else if (encKeys.size() > 1) {
            linkMultiplePeerKeys(encData, encKeys);
        }
        
        encElement.setEncryptedData(encData);
        encElement.getEncryptedKeys().addAll(encKeys);
        
        return encElement;
    }
    
    /**
     * Link a single EncryptedKey to the EncryptedData according to guidelines in SAML Errata E43.
     * 
     * @param encData the EncryptedData
     * @param encKey the EncryptedKey
     */
    protected void linkSinglePeerKey(EncryptedData encData, EncryptedKey encKey) {
        // Forward reference from EncryptedData to the EncryptedKey
        RetrievalMethod rm = 
            (RetrievalMethod) builderFactory
                .getBuilder(RetrievalMethod.DEFAULT_ELEMENT_NAME).buildObject(RetrievalMethod.DEFAULT_ELEMENT_NAME);
        rm.setURI("#" + encKey.getID());
        rm.setType(EncryptionConstants.TYPE_ENCRYPTED_KEY);
        encData.getKeyInfo().getRetrievalMethods().add(rm);
        
        // Back reference from the EncryptedKey to the EncryptedData
        DataReference dr = 
            (DataReference) builderFactory
                .getBuilder(DataReference.DEFAULT_ELEMENT_NAME).buildObject(DataReference.DEFAULT_ELEMENT_NAME);
        dr.setURI("#" + encData.getID());
        encKey.getReferenceList().getDataReferences().add(dr);
    }

    /**
     * Link multiple "multicast" EncryptedKeys to the EncryptedData according 
     * to guidelines in SAML Errata E43.
     * 
     * @param encData the EncryptedData
     * @param encKeys the list of EncryptedKeys
     */
    protected void linkMultiplePeerKeys(EncryptedData encData, List<EncryptedKey> encKeys) {
        // Get the name of the data encryption key
        List<KeyName> dataEncKeyNames = encData.getKeyInfo().getKeyNames();
        String carriedKeyNameValue;
        if (dataEncKeyNames.size() == 0  || DatatypeHelper.isEmpty(dataEncKeyNames.get(0).getValue()) ) {
            // If there isn't one, autogenerate a random key name.
            // TODO - should we do this, or just not use CarriedKeyName at all - what are SAML recs?
            String keyNameValue = idGenerator.generateIdentifier();
            
            KeyName keyName = dataEncKeyNames.get(0);
            if (keyName == null) {
                keyName = (KeyName) builderFactory
                    .getBuilder(KeyName.DEFAULT_ELEMENT_NAME).buildObject(KeyName.DEFAULT_ELEMENT_NAME);
                dataEncKeyNames.add(keyName);
            }
            keyName.setValue(keyNameValue);
            carriedKeyNameValue = keyNameValue;
        } else {
            carriedKeyNameValue = dataEncKeyNames.get(0).getValue();
        }
        
        // Set carried key name of the multicast key in each EncryptedKey
        for (EncryptedKey encKey : encKeys) {
            if (encKey.getCarriedKeyName() == null) {
                CarriedKeyName ckn = (CarriedKeyName) builderFactory
                    .getBuilder(CarriedKeyName.DEFAULT_ELEMENT_NAME)
                    .buildObject(CarriedKeyName.DEFAULT_ELEMENT_NAME);
                encKey.setCarriedKeyName(ckn);
            }
            encKey.getCarriedKeyName().setValue(carriedKeyNameValue);
            
            // Back reference from the EncryptedKeys to the EncryptedData
            DataReference dr = 
                (DataReference) builderFactory
                .getBuilder(DataReference.DEFAULT_ELEMENT_NAME).buildObject(DataReference.DEFAULT_ELEMENT_NAME);
            dr.setURI("#" + encData.getID());
            encKey.getReferenceList().getDataReferences().add(dr);
            
        }
    }

    /**
     * Encrypt the passed XMLObject, using the data encryption parameters previously specified.
     * 
     * @param xmlObject the object to encrypt
     * @return the EncryptedData representing the encrypted XMLObject
     * @throws EncryptionException thrown when the encrypter encounters an error
     */
    protected EncryptedData encryptElement(XMLObject xmlObject) throws EncryptionException {
        EncryptedData encData =  null;
        try {
            encData =  xmlEncrypter.encryptElement(xmlObject, encParams, null);
        } catch (EncryptionException e) {
            log.error("Encrypter could not encrypt the XMLObject: ", e);
            throw e;
        }
        return encData;
    }
    
    /**
     * Generate the wrapped encryption keys, using the data encryption key and key encryption
     * parameters previously specified.
     * 
     * @param containingDocument the document that will own the resulting element
     * @return the list of EncryptedKey's
     * @throws EncryptionException thrown when the encrypter encounters an error
     */
    protected List<EncryptedKey> encryptKeys(Document containingDocument) throws EncryptionException {
        List<EncryptedKey> encKeys = new FastList<EncryptedKey>();
        
        for (KeyEncryptionParameters kekParam: kekParams) {
            EncryptedKey encKey = null;
            try {
                encKey = xmlEncrypter.encryptKey(encParams.getEncryptionKey(), kekParam, containingDocument);
            } catch (EncryptionException e) {
                log.error("Encrypter could not encrypt the data encryption key: ", e);
                throw e;
            }
            encKeys.add(encKey);
        }
        return encKeys;
    }

}
