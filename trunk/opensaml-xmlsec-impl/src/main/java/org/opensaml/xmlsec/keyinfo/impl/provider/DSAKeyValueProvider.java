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

package org.opensaml.xmlsec.keyinfo.impl.provider;

import java.security.KeyException;
import java.security.PublicKey;
import java.util.Collection;

import net.shibboleth.utilities.java.support.collection.LazySet;
import net.shibboleth.utilities.java.support.resolver.CriteriaSet;

import org.opensaml.core.xml.XMLObject;
import org.opensaml.security.SecurityException;
import org.opensaml.security.credential.BasicCredential;
import org.opensaml.security.credential.Credential;
import org.opensaml.security.credential.CredentialContext;
import org.opensaml.security.criteria.KeyAlgorithmCriterion;
import org.opensaml.xmlsec.keyinfo.KeyInfoCredentialResolver;
import org.opensaml.xmlsec.keyinfo.KeyInfoSupport;
import org.opensaml.xmlsec.keyinfo.impl.KeyInfoProvider;
import org.opensaml.xmlsec.keyinfo.impl.KeyInfoResolutionContext;
import org.opensaml.xmlsec.signature.DSAKeyValue;
import org.opensaml.xmlsec.signature.KeyValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of {@link KeyInfoProvider} which supports {@link DSAKeyValue}.
 */
public class DSAKeyValueProvider extends AbstractKeyInfoProvider {
    
    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(DSAKeyValueProvider.class);

    /** {@inheritDoc} */
    public boolean handles(XMLObject keyInfoChild) {
        return getDSAKeyValue(keyInfoChild) != null;
    }

    /** {@inheritDoc} */
    public Collection<Credential> process(KeyInfoCredentialResolver resolver, XMLObject keyInfoChild, 
            CriteriaSet criteriaSet, KeyInfoResolutionContext kiContext) throws SecurityException {
        
        DSAKeyValue keyValue = getDSAKeyValue(keyInfoChild);
        if (keyValue == null) {
            return null;
        }
        
        KeyAlgorithmCriterion algorithmCriteria = criteriaSet.get(KeyAlgorithmCriterion.class);
        if (algorithmCriteria != null 
                && algorithmCriteria.getKeyAlgorithm() != null 
                && ! algorithmCriteria.getKeyAlgorithm().equals("DSA")) {
            log.debug("Criterion specified non-DSA key algorithm, skipping");
            return null;
        }
        
        log.debug("Attempting to extract credential from a DSAKeyValue");
        
        PublicKey pubKey = null;
        try {
            //TODO deal with case of incomplete DSAParams, need hook to resolve those
            pubKey = KeyInfoSupport.getDSAKey(keyValue);
        } catch (KeyException e) {
            log.error("Error extracting DSA key value", e);
            throw new SecurityException("Error extracting DSA key value", e);
        }
        BasicCredential cred = new BasicCredential(pubKey);
        if (kiContext != null) {
            cred.getKeyNames().addAll(kiContext.getKeyNames());
        }
        
        CredentialContext credContext = buildCredentialContext(kiContext);
        if (credContext != null) {
            cred.getCredentalContextSet().add(credContext);
        }
        
        log.debug("Credential successfully extracted from DSAKeyValue");
        LazySet<Credential> credentialSet = new LazySet<Credential>();
        credentialSet.add(cred);
        return credentialSet;
    }
    
    /**
     * Get the DSAKeyValue from the passed XML object.
     * 
     * @param xmlObject an XML object, presumably either a {@link KeyValue} or an {@link DSAKeyValue}
     * @return the DSAKeyValue which was found, or null if none
     */
    protected DSAKeyValue getDSAKeyValue(XMLObject xmlObject) {
        if (xmlObject == null) {return null; }
        
        if (xmlObject instanceof DSAKeyValue) {
            return (DSAKeyValue) xmlObject;
        }
        
        if (xmlObject instanceof KeyValue) {
            return ((KeyValue) xmlObject).getDSAKeyValue();
        }
        return null;
    }
}