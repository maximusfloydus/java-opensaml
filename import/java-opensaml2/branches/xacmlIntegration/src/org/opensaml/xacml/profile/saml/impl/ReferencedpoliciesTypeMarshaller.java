/*
 * Copyright 2008 University Corporation for Advanced Internet Development, Inc.
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

package org.opensaml.xacml.profile.saml.impl;

import org.opensaml.common.impl.AbstractSAMLObjectMarshaller;

/**
 * Marshaller for {@link ReferencedPoliciesType}.
 */
public class ReferencedpoliciesTypeMarshaller extends AbstractSAMLObjectMarshaller {

    /** Constructor. */
    public ReferencedpoliciesTypeMarshaller() {
        super();
    }

    /**
     * Constructor.
     * 
     * @param targetNamespaceURI the namespace URI of either the schema type QName or element QName of the elements this
     *            marshaller operates on
     * @param targetLocalName the local name of either the schema type QName or element QName of the elements this
     *            marshaller operates on
     */
    protected ReferencedpoliciesTypeMarshaller(String targetNamespaceURI, String targetLocalName) {
        super(targetNamespaceURI, targetLocalName);
    }

    
}
