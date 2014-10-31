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

package org.opensaml.xmlsec.config;

import java.util.Iterator;
import java.util.ServiceLoader;

import org.opensaml.core.config.ConfigurationService;
import org.opensaml.core.config.InitializationException;
import org.opensaml.core.config.Initializer;
import org.opensaml.xmlsec.algorithm.AlgorithmDescriptor;
import org.opensaml.xmlsec.algorithm.AlgorithmRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OpenSAML {@link Initializer} implementation for algorithms.
 */
public class GlobalAlgorithmRegistryInitializer implements Initializer {
    
    /** Logger. */
    private Logger log = LoggerFactory.getLogger(GlobalAlgorithmRegistryInitializer.class);

    /** {@inheritDoc} */
    public void init() throws InitializationException {
        AlgorithmRegistry algorithmRegistry = new AlgorithmRegistry();
        
        ServiceLoader<AlgorithmDescriptor> descriptorsLoader = ServiceLoader.load(AlgorithmDescriptor.class);
        Iterator<AlgorithmDescriptor> iter = descriptorsLoader.iterator();
        while (iter.hasNext()) {
            AlgorithmDescriptor descriptor = iter.next();
            log.debug("Registering AlgorithmDescriptor of type '{}' with URI '{}': {}", 
                    descriptor.getType(), descriptor.getURI(), descriptor.getClass().getName());
            algorithmRegistry.register(descriptor);
        }
        
        ConfigurationService.register(AlgorithmRegistry.class, algorithmRegistry);
    }

}