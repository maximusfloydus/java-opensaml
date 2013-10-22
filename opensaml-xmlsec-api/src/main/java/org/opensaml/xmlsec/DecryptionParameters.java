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

package org.opensaml.xmlsec;

import java.util.List;

import javax.annotation.Nullable;

import org.opensaml.xmlsec.encryption.support.EncryptedKeyResolver;
import org.opensaml.xmlsec.keyinfo.KeyInfoCredentialResolver;

/**
 * The effective parameters to use when decrypting encrypted XML.
 */
public class DecryptionParameters {
    
    /** Whitelisted algorithm URIs. */
    private List<String> whiteListedAlgorithmURIs;
    
    /** Blacklisted algorithm URIs. */
    private List<String> blackListedAlgorithmURIs;
    
    /** The EncryptedData's KeyInfo credential resolver. */
    private KeyInfoCredentialResolver dataKeyInfoCredentialResolver;
    
    /** The EncryptedKey's KeyInfo credential resolver. */
    private KeyInfoCredentialResolver kekKeyInfoCredentialResolver;
    
    /** The EncryptedKey resolver. */
    private EncryptedKeyResolver encryptedKeyResolver;
    
    /**
     * Get the list of whitelisted algorithm URI's.
     * 
     * @return the list of algorithms
     */
    @Nullable public List<String> getWhitelistedAlgorithmURIs() {
        return whiteListedAlgorithmURIs;
    }
    
    /**
     * Set the list of whitelisted algorithm URI's.
     * 
     * @param uris the list of algorithms
     */
    public void setWhitelistedAlgorithmURIs(@Nullable final List<String> uris) {
        whiteListedAlgorithmURIs = uris;
    }
    
    /**
     * Get the list of blacklisted algorithm URI's.
     * 
     * @return the list of algorithms
     */
    @Nullable public List<String> getBlacklistedAlgorithmsURIs() {
        return blackListedAlgorithmURIs;
    }
    
    /**
     * Set the list of blacklisted algorithm URI's.
     * 
     * @param uris the list of algorithms
     */
    public void setBlacklistedAlgorithmURIs(@Nullable final List<String> uris) {
        blackListedAlgorithmURIs = uris;
    }
    
    /**
     * Get the KeyInfoCredentialResolver to use when processing the EncryptedData/KeyInfo.
     * 
     * @return the KeyInfoCredentialResolver instance
     */
    @Nullable public KeyInfoCredentialResolver getDataKeyInfoCredentialResolver() {
        return dataKeyInfoCredentialResolver;
    }
    
    /**
     * Set the KeyInfoCredentialResolver to use when processing the EncryptedData/KeyInfo.
     * 
     * @param resolver the KeyInfoCredentialResolver instance
     */
    public void setDataKeyInfoCredentialResolver(@Nullable final KeyInfoCredentialResolver resolver) {
        dataKeyInfoCredentialResolver = resolver;
    }
    
    /**
     * Get the KeyInfoCredentialResolver to use when processing the EncryptedKey/KeyInfo (the
     * Key Encryption Key or KEK).
     * 
     * @return the KeyInfoCredentialResolver instance
     */
    @Nullable public KeyInfoCredentialResolver getKEKKeyInfoCredentialResolver() {
       return kekKeyInfoCredentialResolver; 
    }
    
    /**
     * Set the KeyInfoCredentialResolver to use when processing the EncryptedKey/KeyInfo (the
     * Key Encryption Key or KEK).
     * 
     * @param resolver the KeyInfoCredentialResolver instance
     */
    public void setKEKKeyInfoCredentialResolver(@Nullable final KeyInfoCredentialResolver resolver) {
       kekKeyInfoCredentialResolver = resolver; 
    }
    
    /**
     * Get the EncryptedKeyResolver to use when resolving the EncryptedKey(s) to process.
     * 
     * @return the EncryptedKeyResolver instance
     */
    @Nullable public EncryptedKeyResolver getEncryptedKeyResolver() {
       return encryptedKeyResolver; 
    }
    
    /**
     * Get the EncryptedKeyResolver to use when resolving the EncryptedKey(s) to process.
     * 
     * @param resolver the EncryptedKeyResolver instance
     */
    public void setEncryptedKeyResolver(@Nullable final EncryptedKeyResolver resolver) {
       encryptedKeyResolver = resolver; 
    }
    
}