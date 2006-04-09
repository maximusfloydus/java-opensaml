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

/**
 * 
 */

package org.opensaml.saml2.metadata.impl;

import org.opensaml.common.impl.AbstractSAMLObjectBuilder;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml2.metadata.ManageNameIDService;

/**
 * Builder of {@link org.opensaml.saml2.metadata.impl.ManageNameIDServiceImpl}.
 */
public class ManageNameIDServiceBuilder extends AbstractSAMLObjectBuilder<ManageNameIDService> {

    /**
     * Constructor
     */
    public ManageNameIDServiceBuilder() {

    }

    /*
     * @see org.opensaml.common.impl.AbstractSAMLObjectBuilder#buildObject()
     */
    public ManageNameIDService buildObject() {
        return buildObject(SAMLConstants.SAML20MD_NS, ManageNameIDService.LOCAL_NAME, SAMLConstants.SAML20MD_PREFIX);
    }

    /*
     * @see org.opensaml.xml.XMLObjectBuilder#buildObject(java.lang.String, java.lang.String, java.lang.String)
     */
    public ManageNameIDService buildObject(String namespaceURI, String localName, String namespacePrefix) {
        return new ManageNameIDServiceImpl(namespaceURI, localName, namespacePrefix);
    }
}