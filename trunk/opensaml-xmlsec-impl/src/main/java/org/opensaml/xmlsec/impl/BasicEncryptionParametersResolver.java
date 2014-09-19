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

package org.opensaml.xmlsec.impl;

import java.security.Key;
import java.security.KeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.shibboleth.utilities.java.support.annotation.constraint.NotEmpty;
import net.shibboleth.utilities.java.support.logic.Constraint;
import net.shibboleth.utilities.java.support.resolver.CriteriaSet;
import net.shibboleth.utilities.java.support.resolver.ResolverException;

import org.opensaml.security.credential.Credential;
import org.opensaml.security.credential.CredentialSupport;
import org.opensaml.xmlsec.EncryptionConfiguration;
import org.opensaml.xmlsec.EncryptionParameters;
import org.opensaml.xmlsec.EncryptionParametersResolver;
import org.opensaml.xmlsec.algorithm.AlgorithmRegistry;
import org.opensaml.xmlsec.algorithm.AlgorithmSupport;
import org.opensaml.xmlsec.criterion.EncryptionConfigurationCriterion;
import org.opensaml.xmlsec.criterion.KeyInfoGenerationProfileCriterion;
import org.opensaml.xmlsec.encryption.support.RSAOAEPParameters;
import org.opensaml.xmlsec.keyinfo.KeyInfoGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;

/**
 * Basic implementation of {@link EncryptionParametersResolver}.
 * 
 * <p>
 * The following {@link net.shibboleth.utilities.java.support.resolver.Criterion} inputs are supported:
 * <ul>
 * <li>{@link EncryptionConfigurationCriterion} - required</li> 
 * <li>{@link KeyInfoGenerationProfileCriterion} - optional</li> 
 * </ul>
 * </p>
 */
public class BasicEncryptionParametersResolver extends AbstractSecurityParametersResolver<EncryptionParameters> 
        implements EncryptionParametersResolver {
    
    /** Logger. */
    private Logger log = LoggerFactory.getLogger(BasicEncryptionParametersResolver.class);
    
    /** The AlgorithmRegistry used when processing algorithm URIs. */
    private AlgorithmRegistry algorithmRegistry;
    
    /** Flag indicating whether the resolver should auto-generate data encryption credentials. */
    private boolean autoGenerateDataEncryptionCredential;
    
    /** Constructor. */
    public BasicEncryptionParametersResolver() {
        algorithmRegistry = AlgorithmSupport.getGlobalAlgorithmRegistry();
    }
    
    /**
     * Get the {@link AlgorithmRegistry} instance used when resolving algorithm URIs. Defaults to
     * the registry resolved via {@link AlgorithmSupport#getGlobalAlgorithmRegistry()}.
     * 
     * @return the algorithm registry instance
     */
    public AlgorithmRegistry getAlgorithmRegistry() {
        // Handle case where this resolver was ctored before the library was properly initialized.
        if (algorithmRegistry == null) {
            return AlgorithmSupport.getGlobalAlgorithmRegistry();
        }
        return algorithmRegistry;
    }

    /**
     * Set the {@link AlgorithmRegistry} instance used when resolving algorithm URIs. Defaults to
     * the registry resolved via {@link AlgorithmSupport#getGlobalAlgorithmRegistry()}.
     * 
     * @param registry the new algorithm registry instance
     */
    public void setAlgorithmRegistry(@Nonnull final AlgorithmRegistry registry) {
        algorithmRegistry = Constraint.isNotNull(registry, "AlgorithmRegistry was null");
    }

    /**
     * Get whether an this resolver should auto-generate data encryption credentials.
     * 
     * @return true if should auto-generate, false otherwise
     */
    public boolean isAutoGenerateDataEncryptionCredential() {
        return autoGenerateDataEncryptionCredential;
    }

    /**
     * Set whether an this resolver should auto-generate data encryption credentials.
     * 
     * @param flag true if should auto-generate, false otherwise
     */
    public void setAutoGenerateDataEncryptionCredential(boolean flag) {
        autoGenerateDataEncryptionCredential = flag;
    }

    /** {@inheritDoc} */
    @Override
    @Nonnull public Iterable<EncryptionParameters> resolve(@Nonnull final CriteriaSet criteria) 
            throws ResolverException {
        EncryptionParameters params = resolveSingle(criteria);
        if (params != null) {
            return Collections.singletonList(params);
        } else {
            return Collections.emptyList();
        }
    }

    /** {@inheritDoc} */
    @Override
    @Nullable public EncryptionParameters resolveSingle(@Nonnull final CriteriaSet criteria) throws ResolverException {
        Constraint.isNotNull(criteria, "CriteriaSet was null");
        Constraint.isNotNull(criteria.get(EncryptionConfigurationCriterion.class), 
                "Resolver requires an instance of EncryptionConfigurationCriterion");
        
        Predicate<String> whitelistBlacklistPredicate = getWhitelistBlacklistPredicate(criteria);
        
        EncryptionParameters params = new EncryptionParameters();
        
        resolveAndPopulateCredentialsAndAlgorithms(params, criteria, whitelistBlacklistPredicate);
        
        if (params.getDataEncryptionCredential() != null) {
            params.setDataKeyInfoGenerator(resolveDataKeyInfoGenerator(criteria, params.getDataEncryptionCredential()));
        }
        
        if (params.getKeyTransportEncryptionCredential() != null) {
            params.setKeyTransportKeyInfoGenerator(resolveKeyTransportKeyInfoGenerator(criteria, 
                    params.getKeyTransportEncryptionCredential()));
        }
        
        if (validate(params)) {
            logResult(params);
            return params;
        } else {
            return null;
        }
        
    }

    /**
     * Log the resolved parameters.
     * 
     * @param params the resolved param
     */
    protected void logResult(EncryptionParameters params) {
        if (log.isDebugEnabled()) {
            log.debug("Resolved EncryptionParameters:");
            
            Key keyTransportKey = CredentialSupport.extractEncryptionKey(params.getKeyTransportEncryptionCredential());
            if (keyTransportKey != null) {
                log.debug("\tKey transport credential with key algorithm: {}", keyTransportKey.getAlgorithm());
            } else {
                log.debug("\tKey transport credential: null"); 
            }
            
            log.debug("\tKey transport algorithm URI: {}", params.getKeyTransportEncryptionAlgorithm()); 
            
            log.debug("\tKey transport KeyInfoGenerator: {}", 
                    params.getKeyTransportKeyInfoGenerator() != null ? "present" : "null");
            
            Key dataKey = CredentialSupport.extractEncryptionKey(params.getDataEncryptionCredential());
            if (dataKey != null) {
                log.debug("\tData encryption credential with key algorithm: {}", dataKey.getAlgorithm());
            } else {
                log.debug("\tData encryption credential: null"); 
            }
            
            log.debug("\tData encryption algorithm URI: {}", params.getDataEncryptionAlgorithm());
            
            log.debug("\tData encryption KeyInfoGenerator: {}", 
                    params.getDataKeyInfoGenerator() != null ? "present" : "null");
            
        }
    }

    /**
     * Validate that the {@link EncryptionParameters} instance has all the required properties populated.
     * 
     * @param params the parameters instance to evaluate
     * 
     * @return true if parameters instance passes validation, false otherwise
     */
    protected boolean validate(@Nonnull final EncryptionParameters params) {
        if (params.getKeyTransportEncryptionCredential() == null 
                && params.getDataEncryptionCredential() == null) {
            log.warn("Validation failure: Failed to resolve both a data and a key encryption credential");
            return false;
        }
        if (params.getKeyTransportEncryptionCredential() != null 
                && params.getKeyTransportEncryptionAlgorithm() == null) {
            log.warn("Validation failure: Unable to resolve key encryption algorithm URI for credential");
            return false;
        }
        if (params.getDataEncryptionCredential() != null 
                && params.getDataEncryptionAlgorithm() == null) {
            log.warn("Validation failure: Unable to resolve data encryption algorithm URI for credential");
            return false;
        }
        if (params.getKeyTransportEncryptionCredential() != null 
                && params.getDataEncryptionCredential() == null
                && params.getDataEncryptionAlgorithm() == null) {
            log.warn("Validation failure: Unable to resolve a data encryption algorithm URI " 
                + "for auto-generation of data encryption key");
            return false;
        }
        
        return true;
    }

    /**
     * Get a predicate which implements the effective configured whitelist/blacklist policy.
     * 
     * @param criteria the input criteria being evaluated
     * 
     * @return a whitelist/blacklist predicate instance
     */
    @Nonnull protected Predicate<String> getWhitelistBlacklistPredicate(@Nonnull final CriteriaSet criteria) {
        return resolveWhitelistBlacklistPredicate(criteria, 
                criteria.get(EncryptionConfigurationCriterion.class).getConfigurations());
    }
    
    /**
     * Resolve and populate the data encryption and key transport credentials and algorithm URIs.
     * 
     * @param params the params instance being populated
     * @param criteria the input criteria being evaluated
     * @param whitelistBlacklistPredicate the whitelist/blacklist predicate with which to evaluate the 
     *          candidate data encryption and key transport algorithm URIs
     */
    protected void resolveAndPopulateCredentialsAndAlgorithms(@Nonnull final EncryptionParameters params, 
            @Nonnull final CriteriaSet criteria, @Nonnull final Predicate<String> whitelistBlacklistPredicate) {
        
        // Pre-resolve these for efficiency
        List<Credential> keyTransportCredentials = getEffectiveKeyTransportCredentials(criteria);
        List<String> keyTransportAlgorithms = getEffectiveKeyTransportAlgorithms(criteria, whitelistBlacklistPredicate);
        log.trace("Resolved effective key transport algorithms: {}", keyTransportAlgorithms);
        
        List<Credential> dataEncryptionCredentials = getEffectiveDataEncryptionCredentials(criteria);
        List<String> dataEncryptionAlgorithms = getEffectiveDataEncryptionAlgorithms(criteria, 
                whitelistBlacklistPredicate);
        log.trace("Resolved effective data encryption algorithms: {}", dataEncryptionAlgorithms);
        
        // Select the data encryption algorithm, and credential if exists
        if (dataEncryptionCredentials.isEmpty()) {
            // This is probably the most typical case
            params.setDataEncryptionAlgorithm(resolveDataEncryptionAlgorithm(null, dataEncryptionAlgorithms));
        } else {
            for (Credential dataEncryptionCredential : dataEncryptionCredentials) {
                String dataEncryptionAlgorithm = resolveDataEncryptionAlgorithm(dataEncryptionCredential, 
                        dataEncryptionAlgorithms);
                if (dataEncryptionAlgorithm != null) {
                    params.setDataEncryptionCredential(dataEncryptionCredential);
                    params.setDataEncryptionAlgorithm(dataEncryptionAlgorithm);
                    break;
                } else {
                    log.debug("Unable to resolve data encryption algorithm for credential with key type '{}', " 
                            + "considering other credentials", 
                            CredentialSupport.extractEncryptionKey(dataEncryptionCredential).getAlgorithm());
                }
            }
        }
        
        // Select key encryption cred and algorithm
        for (Credential keyTransportCredential : keyTransportCredentials) {
            String keyTransportAlgorithm = resolveKeyTransportAlgorithm(keyTransportCredential, keyTransportAlgorithms, 
                    params.getDataEncryptionAlgorithm());
            
            if (keyTransportAlgorithm != null) {
                params.setKeyTransportEncryptionCredential(keyTransportCredential);
                params.setKeyTransportEncryptionAlgorithm(keyTransportAlgorithm);
                break;
            } else {
                log.debug("Unable to resolve key transport algorithm for credential with key type '{}', " 
                        + "considering other credentials", 
                        CredentialSupport.extractEncryptionKey(keyTransportCredential).getAlgorithm());
            }
        }
        
        resolveAndPopulateRSAOAEPParams(params, criteria, whitelistBlacklistPredicate);
        
        // Auto-generate data encryption cred if configured and possible
        processDataEncryptionCredentialAutoGeneration(params);
    }
    
    /**
     * Resolve and populate an instance of {@link RSAOAEPParameters}, if appropriate for the selected
     * key transport encryption algorithm.
     * 
     * @param params the params instance being populated
     * @param criteria the input criteria being evaluated
     * @param whitelistBlacklistPredicate the whitelist/blacklist predicate with which to evaluate the 
     *          candidate data encryption and key transport algorithm URIs
     */
    protected void resolveAndPopulateRSAOAEPParams(@Nonnull final EncryptionParameters params, 
            @Nonnull final CriteriaSet criteria,
            @Nonnull final Predicate<String> whitelistBlacklistPredicate) {
        
        if (!AlgorithmSupport.isRSAOAEP(params.getKeyTransportEncryptionAlgorithm())) {
            return;
        }
        
        if (params.getRSAOAEPParameters() == null) {
            params.setRSAOAEPParameters(new RSAOAEPParameters());
        }
        
        RSAOAEPParameters rsaParams = params.getRSAOAEPParameters();
        
        if (rsaParams.isComplete()) {
            return;
        }
        
        populateRSAOAEPParams(rsaParams, criteria, whitelistBlacklistPredicate);
    }

    /**
     * Populate an instance of {@link RSAOAEPParameters} based on data from the supplied instances 
     * of {@link EncryptionConfiguration}.
     * 
     * @param rsaParams the existing RSAOAEPParameters instance being populated
     * @param criteria the input criteria being evaluated
     * @param whitelistBlacklistPredicate the whitelist/blacklist predicate with which to evaluate the 
     *          candidate data encryption and key transport algorithm URIs
     */
    protected void populateRSAOAEPParams(@Nonnull final RSAOAEPParameters rsaParams, 
            @Nonnull final CriteriaSet criteria,
            @Nonnull final Predicate<String> whitelistBlacklistPredicate) {
        
        for (EncryptionConfiguration config : criteria.get(EncryptionConfigurationCriterion.class)
                .getConfigurations()) {
            
            RSAOAEPParameters rsaConfig = config.getRSAOAEPParameters();
            if (rsaConfig != null) {
                if (rsaParams.getDigestMethod() == null) {
                    if (rsaConfig.getDigestMethod() != null 
                            && whitelistBlacklistPredicate.apply(rsaConfig.getDigestMethod())) {
                        rsaParams.setDigestMethod(rsaConfig.getDigestMethod());
                    }
                }
                if (rsaParams.getMaskGenerationFunction() == null) {
                    if (rsaConfig.getMaskGenerationFunction() != null 
                            && whitelistBlacklistPredicate.apply(rsaConfig.getMaskGenerationFunction())) {
                        rsaParams.setMaskGenerationFunction(rsaConfig.getMaskGenerationFunction());
                    }
                }
                if (rsaParams.getOAEPParams() == null) {
                    if (rsaConfig.getOAEPParams() != null) {
                        rsaParams.setOAEPparams(rsaConfig.getOAEPParams());
                    }
                }
            }
            
        }
    }

    /**
     * Determine the key transport encryption algorithm URI to use with the specified key transport credential
     * and optional data encryption algorithm URI.
     * 
     * @param keyTransportCredential the key transport credential being evaluated
     * @param keyTransportAlgorithms the list of effective key transport algorithms to evaluate
     * @param dataEncryptionAlgorithm the optional data encryption algorithm URI to consider
     * 
     * @return the resolved algorithm URI, may be null
     */
    @Nullable protected String resolveKeyTransportAlgorithm(@Nonnull final Credential keyTransportCredential, 
            @Nonnull final List<String> keyTransportAlgorithms, @Nullable final String dataEncryptionAlgorithm) {
        
        //TODO strategy for considering data encryption algorithm URI
        
        if (log.isTraceEnabled()) {
            Key key = CredentialSupport.extractEncryptionKey(keyTransportCredential);
            log.trace("Evaluating key transport encryption credential of type: {}", 
                    key != null ? key.getAlgorithm() : "n/a");
        }
        
        for (String algorithm : keyTransportAlgorithms) {
            log.trace("Evaluating key transport credential against algorithm: {}", algorithm);
            if (credentialSupportsAlgorithm(keyTransportCredential, algorithm) 
                    && isKeyTransportAlgorithm(algorithm))  {
                return algorithm;
            }
        }
        
        return null;
    }

    /**
     * Determine the key transport algorithm URI to use with the specified credential.
     * 
     * @param keyTransportCredential the key transport credential to evaluate
     * @param criteria  the criteria instance being evaluated
     * @param whitelistBlacklistPredicate the whitelist/blacklist predicate with which to evaluate the 
     *          candidate data encryption and key transport algorithm URIs
     * @param dataEncryptionAlgorithm the optional data encryption algorithm URI to consider
     *          
     * @return the selected algorithm URI, may be null
     */
    @Nullable protected String resolveKeyTransportAlgorithm(@Nonnull final Credential keyTransportCredential, 
            @Nonnull final CriteriaSet criteria, @Nonnull final Predicate<String> whitelistBlacklistPredicate,
            @Nullable final String dataEncryptionAlgorithm) {
        
        return resolveKeyTransportAlgorithm(keyTransportCredential, getEffectiveKeyTransportAlgorithms(criteria, 
                whitelistBlacklistPredicate), dataEncryptionAlgorithm);
    }
    
    /**
     * Determine the data encryption algorithm URI, considering the optionally specified data encryption credential.
     * 
     * @param dataEncryptionCredential the data encryption credential being evaluated, may be null
     * @param dataEncryptionAlgorithms the list of effective data encryption algorithms to evaluate
     * 
     * @return the resolved algorithm URI, may be null
     */
    @Nullable protected String resolveDataEncryptionAlgorithm(@Nullable final Credential dataEncryptionCredential, 
            @Nonnull final List<String> dataEncryptionAlgorithms) {
        
        if (log.isTraceEnabled()) {
            Key key = CredentialSupport.extractEncryptionKey(dataEncryptionCredential);
            log.trace("Evaluating data encryption credential of type: {}", 
                    key != null ? key.getAlgorithm() : "n/a");
        }
        
        if (dataEncryptionCredential == null) {
            log.trace("Data encryption credential was null, selecting algorithm based on effective algorithms alone");
            if (!dataEncryptionAlgorithms.isEmpty()) {
                return dataEncryptionAlgorithms.get(0);
            } else {
                return null;
            }
        }
        
        for (String algorithm : dataEncryptionAlgorithms) {
            log.trace("Evaluating data encryption credential against algorithm: {}", algorithm);
            if (credentialSupportsAlgorithm(dataEncryptionCredential, algorithm) 
                    && isDataEncryptionAlgorithm(algorithm))  {
                return algorithm;
            }
        }
        
        return null;
    }

    /**
     * Determine the data encryption algorithm URI to use with the specified data encryption credential.
     * 
     * @param dataEncryptionCredential the data encryption credential to evaluate
     * @param criteria  the criteria instance being evaluated
     * @param whitelistBlacklistPredicate the whitelist/blacklist predicate with which to evaluate the 
     *          candidate data encryption and key transport algorithm URIs
     *          
     * @return the selected algorithm URI
     */
    @Nullable protected String resolveDataEncryptionAlgorithm(@Nonnull final Credential dataEncryptionCredential, 
            @Nonnull final CriteriaSet criteria, @Nonnull final Predicate<String> whitelistBlacklistPredicate) {
        
        return resolveDataEncryptionAlgorithm(dataEncryptionCredential, getEffectiveDataEncryptionAlgorithms(criteria,
                whitelistBlacklistPredicate));
    }
    
    /**
     * Get the effective list of data encryption credentials to consider.
     * 
     * @param criteria the input criteria being evaluated
     * 
     * @return the list of credentials
     */
    @Nonnull protected List<Credential> getEffectiveDataEncryptionCredentials(@Nonnull final CriteriaSet criteria) {
        ArrayList<Credential> accumulator = new ArrayList<>();
        for (EncryptionConfiguration config : criteria.get(EncryptionConfigurationCriterion.class)
                .getConfigurations()) {
            
            accumulator.addAll(config.getDataEncryptionCredentials());
            
        }
        return accumulator;
    }
    
    /**
     * Get the effective list of data encryption algorithm URIs to consider, including application of 
     * whitelist/blacklist policy.
     * 
     * @param criteria the input criteria being evaluated
     * @param whitelistBlacklistPredicate  the whitelist/blacklist predicate to use
     * 
     * @return the list of effective algorithm URIs
     */
    @Nonnull protected List<String> getEffectiveDataEncryptionAlgorithms(@Nonnull final CriteriaSet criteria, 
            @Nonnull final Predicate<String> whitelistBlacklistPredicate) {
        
        ArrayList<String> accumulator = new ArrayList<>();
        for (EncryptionConfiguration config : criteria.get(EncryptionConfigurationCriterion.class)
                .getConfigurations()) {
            
            accumulator.addAll(Collections2.filter(config.getDataEncryptionAlgorithms(), 
                    Predicates.and(getAlgorithmRuntimeSupportedPredicate(), whitelistBlacklistPredicate)));
            
        }
        return accumulator;
    }
    
    /**
     * Get the effective list of key transport credentials to consider.
     * 
     * @param criteria the input criteria being evaluated
     * 
     * @return the list of credentials
     */
    @Nonnull protected List<Credential> getEffectiveKeyTransportCredentials(@Nonnull final CriteriaSet criteria) {
        ArrayList<Credential> accumulator = new ArrayList<>();
        for (EncryptionConfiguration config : criteria.get(EncryptionConfigurationCriterion.class)
                .getConfigurations()) {
            
            accumulator.addAll(config.getKeyTransportEncryptionCredentials());
            
        }
        return accumulator;
    }
    
    /**
     * Get the effective list of key transport algorithm URIs to consider, including application of 
     * whitelist/blacklist policy.
     * 
     * @param criteria the input criteria being evaluated
     * @param whitelistBlacklistPredicate  the whitelist/blacklist predicate to use
     * 
     * @return the list of effective algorithm URIs
     */
    @Nonnull protected List<String> getEffectiveKeyTransportAlgorithms(@Nonnull final CriteriaSet criteria, 
            @Nonnull final Predicate<String> whitelistBlacklistPredicate) {
        
        ArrayList<String> accumulator = new ArrayList<>();
        for (EncryptionConfiguration config : criteria.get(EncryptionConfigurationCriterion.class)
                .getConfigurations()) {
            
            accumulator.addAll(Collections2.filter(config.getKeyTransportEncryptionAlgorithms(), 
                    Predicates.and(getAlgorithmRuntimeSupportedPredicate(), whitelistBlacklistPredicate)));
            
        }
        return accumulator;
    }
    
    /**
     * Resolve and return the {@link KeyInfoGenerator} instance to use with the specified data encryption credential.
     * 
     * @param criteria the input criteria being evaluated
     * @param dataEncryptionCredential the credential being evaluated
     * 
     * @return KeyInfo generator instance, or null
     */
    @Nullable protected KeyInfoGenerator resolveDataKeyInfoGenerator(@Nullable final CriteriaSet criteria, 
            @Nullable final Credential dataEncryptionCredential) {
        if (dataEncryptionCredential == null) {
            return null;
        }
        
        String name = null;
        if (criteria.get(KeyInfoGenerationProfileCriterion.class) != null) {
            name = criteria.get(KeyInfoGenerationProfileCriterion.class).getName();
        }
        
        for (EncryptionConfiguration config : criteria.get(EncryptionConfigurationCriterion.class)
                .getConfigurations()) {
            
            KeyInfoGenerator kig = lookupKeyInfoGenerator(dataEncryptionCredential, 
                    config.getDataKeyInfoGeneratorManager(), name);
            if (kig != null) {
                return kig;
            }
            
        }
        
        return null;
    }

    /**
     * Resolve and return the {@link KeyInfoGenerator} instance to use with the specified key transport credential.
     * 
     * @param criteria the input criteria being evaluated
     * @param keyTransportEncryptionCredential the credential being evaluated
     * 
     * @return KeyInfo generator instance, or null
     */
    @Nullable protected KeyInfoGenerator resolveKeyTransportKeyInfoGenerator(@Nonnull final CriteriaSet criteria,
            @Nullable final Credential keyTransportEncryptionCredential) {
        if (keyTransportEncryptionCredential == null) {
            return null;
        }
        
        String name = null;
        if (criteria.get(KeyInfoGenerationProfileCriterion.class) != null) {
            name = criteria.get(KeyInfoGenerationProfileCriterion.class).getName();
        }
        
        for (EncryptionConfiguration config : criteria.get(EncryptionConfigurationCriterion.class)
                .getConfigurations()) {
            
            KeyInfoGenerator kig = lookupKeyInfoGenerator(keyTransportEncryptionCredential, 
                    config.getKeyTransportKeyInfoGeneratorManager(), name);
            if (kig != null) {
                return kig;
            }
            
        }
        
        return null;
    }
    
    /**
     * Get a predicate which evaluates whether a cryptographic algorithm is supported
     * by the runtime environment.
     * 
     * @return the predicate
     */
    @Nonnull protected Predicate<String> getAlgorithmRuntimeSupportedPredicate() {
        return new AlgorithmRuntimeSupportedPredicate(getAlgorithmRegistry());
    }
    
    /**
     * Evaluate whether the specified credential is supported for use with the specified algorithm URI.
     * 
     * @param credential the credential to evaluate
     * @param algorithm the algorithm URI to evaluate
     * 
     * @return true if credential may be used with the supplied algorithm URI, false otherwise
     */
    protected boolean credentialSupportsAlgorithm(@Nonnull final Credential credential, 
            @Nonnull @NotEmpty final String algorithm) {
        
        return AlgorithmSupport.credentialSupportsAlgorithmForEncryption(credential, 
                getAlgorithmRegistry().get(algorithm));
    }
    
    /**
     * Evaluate whether the specified algorithm is a key transport algorithm.
     * 
     * @param algorithm the algorithm URI to evaluate
     * 
     * @return true if is a key transport algorithm URI, false otherwise
     */
    protected boolean isKeyTransportAlgorithm(@Nonnull final String algorithm) {
        
        return AlgorithmSupport.isKeyEncryptionAlgorithm(getAlgorithmRegistry().get(algorithm));
    }
    
    /**
     * Evaluate whether the specified algorithm is a data encryption algorithm.
     * 
     * @param algorithm the algorithm URI to evaluate
     * 
     * @return true if is a key transport algorithm URI, false otherwise
     */
    protected boolean isDataEncryptionAlgorithm(String algorithm) {
        
        return AlgorithmSupport.isDataEncryptionAlgorithm(getAlgorithmRegistry().get(algorithm));
    }

    /**
     * Generate a random data encryption symmetric key credential.
     * 
     * @param dataEncryptionAlgorithm the data encryption algorithm URI
     * 
     * @return the generated credential, or null if there was a problem generating a key from the algorithm URI
     */
    @Nullable protected Credential generateDataEncryptionCredential(@Nonnull final String dataEncryptionAlgorithm) {
        try {
            return AlgorithmSupport.generateSymmetricKeyAndCredential(dataEncryptionAlgorithm);
        } catch (NoSuchAlgorithmException | KeyException e) {
            log.warn("Error generating a symmetric key credential using algorithm URI: " + dataEncryptionAlgorithm, e);
            return null;
        }
    }
    
    /**
     * Auto-generate and populate a data encryption credential, if configured and required conditions
     * are met.
     * 
     * @param params the encryption parameters instance to process
     */
    protected void processDataEncryptionCredentialAutoGeneration(@Nonnull final EncryptionParameters params) {
        if (isAutoGenerateDataEncryptionCredential() 
                && params.getKeyTransportEncryptionCredential() != null 
                && params.getDataEncryptionCredential() == null
                && params.getDataEncryptionAlgorithm() != null) {
            
            log.debug("Auto-generating data encryption credential using algorithm URI: {}", 
                    params.getDataEncryptionAlgorithm());
            
            params.setDataEncryptionCredential(
                    generateDataEncryptionCredential(params.getDataEncryptionAlgorithm()));
        } 
    }

}
