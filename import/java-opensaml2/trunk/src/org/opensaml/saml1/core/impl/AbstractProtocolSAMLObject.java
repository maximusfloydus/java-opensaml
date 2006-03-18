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

package org.opensaml.saml1.core.impl;

import org.opensaml.common.SAMLVersion;
import org.opensaml.common.impl.AbstractSAMLObject;
import org.opensaml.common.xml.SAMLConstants;

/**
 * Base for SAML 1.X Protocol SAMLObjects.
 */
public abstract class AbstractProtocolSAMLObject extends AbstractSAMLObject {

    /**
     * Constructor. Sets namespace to {@link SAMLConstants#SAML1P_NS} and prefix to
     * {@link SAMLConstants#SAML1P_PREFIX}.  Sets the SAML version to {@link SAMLVersion#VERSION_11}.
     * 
     * @param localName the local name of the element
     * @param version the version to set
     */
    protected AbstractProtocolSAMLObject(String localName, SAMLVersion version) {
        super(SAMLConstants.SAML1P_NS, localName, SAMLConstants.SAML1P_PREFIX);
        setSAMLVersion(version);
    }

    /**
     * Constructor.  Sets the SAML version to {@link SAMLVersion#VERSION_11}.
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     * @param version the version to set
     */
    protected AbstractProtocolSAMLObject(String namespaceURI, String elementLocalName, String namespacePrefix, SAMLVersion version) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        setSAMLVersion(version);
    }
}