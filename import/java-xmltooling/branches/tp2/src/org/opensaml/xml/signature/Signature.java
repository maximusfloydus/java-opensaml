/*
 * Copyright [2005] [University Corporation for Advanced Internet Development, Inc.]
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

package org.opensaml.xml.signature;

import java.security.Key;
import java.util.List;

import javax.xml.namespace.QName;

import javolution.util.FastList;

import org.apache.xml.security.signature.XMLSignature;
import org.opensaml.xml.AbstractXMLObject;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.util.XMLConstants;

/**
 * XMLObject representing an enveloped or detached XML Digital Signature, version 20020212, Signature element.
 */
public class Signature extends AbstractXMLObject {

    /** Element local name */
    public final static String DEFAULT_ELEMENT_LOCAL_NAME = "Signature";
    
    /** Default element name */
    public final static QName DEFAULT_ELEMENT_NAME = new QName(XMLConstants.XMLSIG_NS, DEFAULT_ELEMENT_LOCAL_NAME, XMLConstants.XMLSIG_PREFIX);
    
    /** Local name of the XSI type */
    public final static String TYPE_LOCAL_NAME = "SignatureType"; 
        
    /** QName of the XSI type */
    public final static QName TYPE_NAME = new QName(XMLConstants.XMLSIG_NS, TYPE_LOCAL_NAME, XMLConstants.XMLSIG_PREFIX);
    
    /** XML XMLSecSignatureImpl construct */
    private XMLSignature signature;
    
    /** Canonicalization algorithm used in signature */
    private String canonicalizationAlgorithm;
    
    /** Algorithm used to generate the signature */
    private String signatureAlgorithm;
    
    /** Key used to sign the signature */
    private Key signingKey;
    
    /** Public key information to embed in the signature */
    private KeyInfo keyInfo;
    
    /** References to content to be signed */
    private List<ContentReference> contentReferences;
    
    /**
     * Constructor
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected Signature(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        contentReferences = new FastList<ContentReference>();
    }

    /**
     * Gets the canonicalization algorithm used to create the signature content.
     * 
     * @return the canonicalization algorithm used to create the signature content
     */
    public String getCanonicalizationAlgorithm() {
        return canonicalizationAlgorithm;
    }


    /**
     * Sets the canonicalization algorithm used to create the signature content.
     * 
     * @param newAlgorithm the canonicalization algorithm used to create the signature content
     */
    public void setCanonicalizationAlgorithm(String newAlgorithm) {
        canonicalizationAlgorithm = newAlgorithm;
    }

    /**
     * Gets the algorithm used to compute the signature.
     * 
     * @return the algorithm used to compute the signature
     */
    public String getSignatureAlgorithm() {
        return signatureAlgorithm;
    }

    /**
     * Sets the algorithm used to compute the signature.
     * 
     * @param newAlgorithm the algorithm used to compute the signature
     */
    public void setSignatureAlgorithm(String newAlgorithm) {
        signatureAlgorithm  = newAlgorithm;
    }

    /**
     * Gets the signature signing key.
     * 
     * @return the signature signing key
     */
    public Key getSigningKey() {
        return signingKey;
    }

    /**
     * Sets the signature signing key.
     * 
     * @param newKey the signature signing key
     */
    public void setSigningKey(Key newKey) {
        signingKey = newKey;
    }

    /**
     * Gets the key info added to this signature.
     * 
     * @return the key info added to this signature
     */
    public KeyInfo getKeyInfo() {
        return keyInfo;
    }

    /**
     * Sets the key info added to this signature.
     * 
     * @param newKeyInfo  the key info added to this signature
     */
    public void setKeyInfo(KeyInfo newKeyInfo) {
        keyInfo = newKeyInfo;
    }

    /**
     * Gets the list of signature content references.
     * 
     * @return the list of signature content references
     */
    public List<ContentReference> getContentReferences() {
        return contentReferences;
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        // Children
        return null;
    }
    
    /**
     * Gets the Apache XMLSec signature object backing this signature.
     * 
     * @return the Apache XMLSec signature object backing this signature
     */
    protected XMLSignature getXMLSignature(){
        return signature;
    }
    
    /**
     * Sets the Apache XMLSec signature object backing this signature.
     * 
     * @param newSignature the Apache XMLSec signature object backing this signature
     */
    protected void setXMLSignature(XMLSignature newSignature){
        signature = newSignature;
    }
}