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

package org.opensaml.saml2.core.impl;

import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml2.core.NameID;

/**
 * A thread safe Marshaller for {@link org.opensaml.saml2.core.NameID} objects.
 */
public class NameIDMarshaller extends AbstractNameIDTypeMarshaller {

    /** Constructor */
    public NameIDMarshaller() {
        super(SAMLConstants.SAML20_NS, NameID.DEFAULT_ELEMENT_LOCAL_NAME);
    }

    /**
     * 
     * Constructor
     * 
     * @param targetNamespaceURI
     * @param targetLocalName
     */
    protected NameIDMarshaller(String targetNamespaceURI, String targetLocalName) {
        super(targetNamespaceURI, targetLocalName);
    }
}