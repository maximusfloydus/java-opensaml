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

package org.opensaml.xml.encryption;

import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.xml.namespace.QName;

import org.apache.xml.security.Init;
import org.apache.xml.security.algorithms.JCEMapper;
import org.apache.xml.security.encryption.XMLCipher;
import org.apache.xml.security.encryption.XMLEncryptionException;

import org.opensaml.xml.Configuration;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.XMLObjectBuilder;
import org.opensaml.xml.io.Marshaller;
import org.opensaml.xml.io.MarshallingException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * A class for encrypting XMLObjects, their content, and keys.
 */
public class Encrypter {

    /**
     * Encrypts the DOM representation of the XMLObject.
     * 
     * @param xmlObject the XMLObject to be encrypted
     * @param encParams parameters for encrypting the data
     * @param kekParams parameters for encrypting the encryption key
     * 
     * @return the resulting EncryptedData element
     * @throws Exception 
     */
    public EncryptedData encryptElement(XMLObject xmlObject, EncryptionParameters encParams, KeyEncryptionParameters kekParams) throws Exception{
        return encryptElement(xmlObject, encParams, kekParams, false);
    }
    
    /**
     * Encrypts the DOM representation of the content of an XMLObject.
     * 
     * @param xmlObject the XMLObject to be encrypted
     * @param encParams parameters for encrypting the data
     * @param kekParams parameters for encrypting the encryption key
     * 
     * @return the resulting EncryptedData element
     * @throws Exception 
     */
    public EncryptedData encryptElementContent(XMLObject xmlObject, EncryptionParameters encParams, KeyEncryptionParameters kekParams) throws Exception{
        return encryptElement(xmlObject, encParams, kekParams, true);
    }
    
    /**
     * Encrypts the DOM representation of the content of an XMLObject.  The result is then added as a child of 
     * an XMLObject of the given type.  If <code>replaceInline</code> is true then the newly created XMLObject will 
     * replace the XMLObject that was encrypted.  This must be performed prior to any digital signature being computed 
     * over this new XMLObject or the signature will be invalidated.
     * 
     * @param xmlObject the XMLObject to encryption
     * @param encParams parameters for encrypting the data
     * @param kekParams parameters for encrypting the encryption key
     * @param envelopingXMLObject the QName of the new XMLObject that will hold the encrypted data
     * @param replaceInline true if the new eneveloping XMLObject should replace the encrypted XMLObject within the marshalled tree
     * 
     * @return the data generated by encrypting the given XMLObject
     * 
     * @throws Exception
     */
    public Element encryptElement(XMLObject xmlObject, EncryptionParameters encParams, KeyEncryptionParameters kekParams, QName envelopingXMLObject, boolean replaceInline) throws Exception{
        return null;
    }
    
    /**
     * Encrypts a key used for encrypting an XMLObject.
     * 
     * @param key the key to encrypt
     * @param containingDocument the document that will own the resulting element
     * @param kekParams parameters for encrypting the key
     * 
     * @return the resulting EncryptedKey element
     * 
     * @throws XMLEncryptionException 
     */
    public EncryptedKey encryptKey(Key key, KeyEncryptionParameters kekParams, Document containingDocument) throws XMLEncryptionException{
//        XMLCipher xmlCipher = XMLCipher.getInstance(keyEncParams.getAlgorithm());
//        xmlCipher.init(XMLCipher.WRAP_MODE, keyEncParams.getEncryptionKey());
//        org.apache.xml.security.encryption.EncryptedKey encryptedKey = xmlCipher.encryptKey(containingDocument, key);
//        return xmlCipher.martial(encryptedKey);
        return null;
    }
    
    /**
     * Encrypts the given XMLObject
     * 
     * @param xmlObject the XMLObject to be encrypted
     * @param encParams the encryption parameters to use
     * @param encryptContentMode whether just the content of the XMLObject should be encrypted
     * 
     * @return the resulting EncryptedData element
     * @throws MarshallingException 
     * @throws XMLEncryptionException 
     */
    private EncryptedData encryptElement(XMLObject xmlObject, EncryptionParameters encParams, KeyEncryptionParameters kekParams, boolean encryptContentMode) throws Exception{
        //TODO this will all change when schema XMLObjects are implemented.
        checkParams(encParams, kekParams);
        
        Element targetElement = xmlObject.getDOM();
        if(targetElement == null){
            Marshaller marshaller = Configuration.getMarshallerFactory().getMarshaller(xmlObject);
            targetElement = marshaller.marshall(xmlObject);
        }
        
        //TODO need to check on DOM node ownership issues - partially pending API question
        XMLCipher xmlCipher = XMLCipher.getInstance(encParams.getAlgorithm());
        xmlCipher.init(XMLCipher.ENCRYPT_MODE, encParams.getEncryptionKey());
        org.apache.xml.security.encryption.EncryptedData encryptedData = xmlCipher.encryptData(targetElement.getOwnerDocument(), targetElement, encryptContentMode);
        
        if (kekParams != null && kekParams.getEncryptionKey() != null) {
            XMLCipher keyCipher = XMLCipher.getInstance(kekParams.getAlgorithm());
            keyCipher.init(XMLCipher.WRAP_MODE, kekParams.getEncryptionKey());
            org.apache.xml.security.encryption.EncryptedKey encryptedKey = keyCipher.encryptKey(targetElement.getOwnerDocument(), encParams.getEncryptionKey());
            //TODO deal with existing KeyInfo - pending API question
            org.apache.xml.security.keys.KeyInfo keyInfo = new org.apache.xml.security.keys.KeyInfo(targetElement.getOwnerDocument());
            keyInfo.add(encryptedKey);
            encryptedData.setKeyInfo(keyInfo);
        }
        
        XMLObjectBuilder builder = Configuration.getBuilderFactory().getBuilder(EncryptedData.DEFAULT_ELEMENT_NAME);
        EncryptedData encDataXMLObject = (EncryptedData) builder.buildObject(EncryptedData.DEFAULT_ELEMENT_NAME);
        //encDataXMLObject.setXMLEncData(encryptedData);
        
        return encDataXMLObject;
        
    }
    
    private void checkParams(EncryptionParameters encParams, KeyEncryptionParameters kekParams) throws EncryptionException {
        
        if (encParams == null ) {
            throw new EncryptionException("EncryptionParameters argument was null");
        }
        
        if (encParams.getEncryptionKey() == null && (kekParams == null || kekParams.getEncryptionKey() == null)) {
            throw new EncryptionException("Using a generated encryption key requires a KeyEncryptionParameters object and key");
        }
        
        if (encParams.getEncryptionKey() == null) {
            String jceAlgorithm = JCEMapper.getJCEKeyAlgorithmFromURI(encParams.getAlgorithm());
            int keyLength = JCEMapper.getKeyLengthFromURI(encParams.getAlgorithm());
            try {
                KeyGenerator keyGenerator = KeyGenerator.getInstance(jceAlgorithm);
                keyGenerator.init(keyLength);
                encParams.setEncryptionKey(keyGenerator.generateKey()); 
            } catch (NoSuchAlgorithmException e) {
                throw new EncryptionException("Algorithm URI not found when generating encryption key: " + encParams.getAlgorithm());
            }
        }
        
        if (encParams.getEncryptionKey() == null) {
            throw new EncryptionException("No encryption key was generated by KeyGenerator");
        }
    }
    
    /*
     * Initialize the Apache XML security library if it hasn't been already
     */
    static {
        if (!Init.isInitialized()) {
            Init.init();
        }
    }
}