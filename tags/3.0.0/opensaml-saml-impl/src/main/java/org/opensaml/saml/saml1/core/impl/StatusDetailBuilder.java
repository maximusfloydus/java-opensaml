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

package org.opensaml.saml.saml1.core.impl;

import org.opensaml.saml.common.AbstractSAMLObjectBuilder;
import org.opensaml.saml.common.xml.SAMLConstants;
import org.opensaml.saml.saml1.core.StatusDetail;

/**
 * Builder of {@link org.opensaml.saml.saml1.core.impl.StatusDetailImpl}.
 */
public class StatusDetailBuilder extends AbstractSAMLObjectBuilder<StatusDetail> {

    /**
     * Constructor.
     */
    public StatusDetailBuilder() {

    }

    /**
     * {@inheritDoc}
     */
    public StatusDetail buildObject() {
        return buildObject(SAMLConstants.SAML10P_NS, StatusDetail.DEFAULT_ELEMENT_LOCAL_NAME,
                SAMLConstants.SAML1P_PREFIX);
    }

    /**
     * {@inheritDoc}
     */
    public StatusDetail buildObject(String namespaceURI, String localName, String namespacePrefix) {
        return new StatusDetailImpl(namespaceURI, localName, namespacePrefix);
    }
}