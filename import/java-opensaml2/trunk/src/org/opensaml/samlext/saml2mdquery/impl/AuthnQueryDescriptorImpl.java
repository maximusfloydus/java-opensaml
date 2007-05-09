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

package org.opensaml.samlext.saml2mdquery.impl;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.opensaml.saml2.metadata.Endpoint;
import org.opensaml.samlext.saml2mdquery.AuthnQueryDescriptor;

/**
 * Concrete implementation of {@link AuthnQueryDescriptor}.
 */
public class AuthnQueryDescriptorImpl extends QueryDescriptorTypeImpl implements AuthnQueryDescriptor{
    
    /**
     * Constructor.
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected AuthnQueryDescriptorImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
    }
    
    /** {@inheritDoc} */
    public List<Endpoint> getEndpoints() {
        return new ArrayList<Endpoint>();
    }
    
    /** {@inheritDoc} */
    public List<Endpoint> getEndpoints(QName type) {
        return null;
    }
}