/*
 * Licensed to the University Corporation for Advanced Internet Development, 
 * Inc. (UCAID) under one or more contributor license agreements.  See the 
 * NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The UCAID licenses this file to You under the Apache 
 * License, Version 2.0 (the "License"); you may not use this file except in 
 * compliance with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opensaml.xmlsec.encryption.support;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.Assert;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyPair;

import net.shibboleth.utilities.java.support.xml.XMLParserException;

import org.apache.xml.security.utils.IdResolver;
import org.opensaml.core.xml.XMLObject;
import org.opensaml.core.xml.XMLObjectBaseTestCase;
import org.opensaml.core.xml.config.XMLObjectProviderRegistrySupport;
import org.opensaml.core.xml.io.Marshaller;
import org.opensaml.core.xml.io.MarshallingException;
import org.opensaml.core.xml.io.UnmarshallingException;
import org.opensaml.security.credential.Credential;
import org.opensaml.security.credential.CredentialSupport;
import org.opensaml.security.crypto.KeySupport;
import org.opensaml.xmlsec.algorithm.AlgorithmSupport;
import org.opensaml.xmlsec.encryption.EncryptedData;
import org.opensaml.xmlsec.keyinfo.KeyInfoCredentialResolver;
import org.opensaml.xmlsec.keyinfo.impl.StaticKeyInfoCredentialResolver;
import org.opensaml.xmlsec.mock.SignableSimpleXMLObject;
import org.opensaml.xmlsec.signature.Signature;
import org.opensaml.xmlsec.signature.support.DocumentInternalIDContentReference;
import org.opensaml.xmlsec.signature.support.SignatureConstants;
import org.opensaml.xmlsec.signature.support.SignatureException;
import org.opensaml.xmlsec.signature.support.SignatureValidator;
import org.opensaml.xmlsec.signature.support.Signer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Test decryption of signed content.
 */
public class DecryptionSignedContentTest extends XMLObjectBaseTestCase {

    /** Credential used to sign and verify. */
    private Credential signingCredential;

    /** The data encryption parameters object. */
    private DataEncryptionParameters encParams;

    /** Resolver for the data encryption key. */
    private KeyInfoCredentialResolver encKeyResolver;

    /** The ID value used as the signature Reference URI attribute value, set on root SimpleXMLObject. */
    private String idValue;

    @BeforeMethod
    protected void setUp() throws Exception {
        KeyPair keyPair = KeySupport.generateKeyPair("RSA", 1024, null);
        signingCredential = CredentialSupport.getSimpleCredential(keyPair.getPublic(), keyPair.getPrivate());

        String encURI = EncryptionConstants.ALGO_ID_BLOCKCIPHER_AES128;
        Credential encCred = AlgorithmSupport.generateSymmetricKeyAndCredential(encURI);
        encParams = new DataEncryptionParameters();
        encParams.setAlgorithm(encURI);
        encParams.setEncryptionCredential(encCred);
        encKeyResolver = new StaticKeyInfoCredentialResolver(encCred);

        idValue = "IDValueFoo";
    }

    /**
     * Test decryption of signed object and then verify signature.
     * 
     * @throws IOException
     * @throws SignatureException 
     */
    @Test
    public void testDecryptAndVerifySignedElement() throws MarshallingException, 
            UnmarshallingException, EncryptionException, DecryptionException, XMLParserException, IOException, SignatureException {
        // Get signed element
        Element signedElement = getSignedElement();

        // Unmarshall to XMLObject
        XMLObject signedXMLObject = unmarshallerFactory.getUnmarshaller(signedElement).unmarshall(signedElement);
        Assert.assertTrue(signedXMLObject instanceof SignableSimpleXMLObject);
        SignableSimpleXMLObject sxo = (SignableSimpleXMLObject) signedXMLObject;

        // Encrypt object
        Encrypter encrypter = new Encrypter();
        EncryptedData encryptedData = encrypter.encryptElement(sxo, encParams);

        // Dump EncryptedData to temp file and reparse and unmarshall, just to eliminate any possible side effects
        // or error possibilities re: accidentially reusing the existing cached DOM
        // or the XMLObject instances.
        File tempfile = File.createTempFile("encdata", ".xml");
        printXML(encryptedData, tempfile.getAbsolutePath());
        InputStream input = new FileInputStream(tempfile);
        Document document = parserPool.parse(input);
        tempfile.delete();
        Element encDataElement = document.getDocumentElement();
        XMLObject encryptedXMLObject = unmarshallerFactory.getUnmarshaller(encDataElement).unmarshall(encDataElement);
        Assert.assertTrue(encryptedXMLObject instanceof EncryptedData);
        EncryptedData encryptedData2 = (EncryptedData) encryptedXMLObject;

        // Decrypt object. Use 2-arg variant to make decrypted element
        // the root of a new Document.
        Decrypter decrypter = new Decrypter(encKeyResolver, null, null);
        XMLObject decryptedXMLObject = decrypter.decryptData(encryptedData2, true);
        Assert.assertTrue(decryptedXMLObject instanceof SignableSimpleXMLObject);
        SignableSimpleXMLObject decryptedSXO = (SignableSimpleXMLObject) decryptedXMLObject;

        Signature decryptedSignature = decryptedSXO.getSignature();

        // Sanity check that DOM-based ID resolution using Apache XML Security IdResolver
        // is working correctly
        Element apacheResolvedElement = IdResolver.getElementById(decryptedSignature.getDOM().getOwnerDocument(),
                idValue);
        Assert.assertNotNull(apacheResolvedElement, "Apache ID resolver found no element");
        Assert.assertTrue(decryptedSXO.getDOM()
                .isSameNode(apacheResolvedElement), "Apache ID resolver found different element");

        // Verify signature of the decrypted content - this is where bug was reported.
        SignatureValidator.validate(decryptedSignature, signingCredential);
    }

    /** Just a sanity check that unit test is set up correctly. 
     * @throws SignatureException */
    @Test
    public void testPlainRoundTripSignature() throws MarshallingException, UnmarshallingException, SignatureException {
        Element signedElement = getSignedElement();

        XMLObject xmlObject = unmarshallerFactory.getUnmarshaller(signedElement).unmarshall(signedElement);
        Assert.assertTrue(xmlObject instanceof SignableSimpleXMLObject);
        SignableSimpleXMLObject sxo = (SignableSimpleXMLObject) xmlObject;

        try {
            SignatureValidator.validate(sxo.getSignature(), signingCredential);
        } catch (SignatureException e) {
            Assert.fail("Signature validation failed: " + e);
        }
    }

    /**
     * Creates a signed SimpleXMLObject element.
     * 
     * @return a XMLObject that has a Signature child element
     * @throws MarshallingException
     * @throws SignatureException 
     */
    private Element getSignedElement() throws MarshallingException, SignatureException {
        SignableSimpleXMLObject sxo = (SignableSimpleXMLObject) buildXMLObject(SignableSimpleXMLObject.ELEMENT_NAME);
        sxo.setId(idValue);

        Signature sig = (Signature) buildXMLObject(Signature.DEFAULT_ELEMENT_NAME);
        sig.setSigningCredential(signingCredential);
        sig.setCanonicalizationAlgorithm(SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);
        sig.setSignatureAlgorithm(SignatureConstants.ALGO_ID_SIGNATURE_RSA);

        DocumentInternalIDContentReference contentReference = new DocumentInternalIDContentReference(idValue);
        contentReference.getTransforms().add(SignatureConstants.TRANSFORM_ENVELOPED_SIGNATURE);
        contentReference.getTransforms().add(SignatureConstants.TRANSFORM_C14N_EXCL_OMIT_COMMENTS);
        contentReference.setDigestAlgorithm(SignatureConstants.ALGO_ID_DIGEST_SHA1);
        sig.getContentReferences().add(contentReference);

        sxo.setSignature(sig);

        Marshaller marshaller = XMLObjectProviderRegistrySupport.getMarshallerFactory().getMarshaller(sxo);
        Element signedElement = marshaller.marshall(sxo);

        Signer.signObject(sig);
        return signedElement;
    }

}
