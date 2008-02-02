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

import org.opensaml.common.SAMLObject;
import org.opensaml.common.impl.AbstractSAMLObjectBuilder;
import org.opensaml.xacml.XACMLObjectBuilder;
import org.opensaml.xacml.profile.saml.ReferencedPoliciesType;
import org.opensaml.xml.XMLObject;

/**
 * Builder for {@link ReferencedPoliciesType}.
 */
public class ReferencedPoliciesTypeImplBuilder extends AbstractSAMLObjectBuilder<ReferencedPoliciesType> 
implements XACMLObjectBuilder<ReferencedPoliciesType>{

    /** {@inheritDoc} */
    public ReferencedPoliciesType buildObject() {
        return null;
    }

    /** {@inheritDoc} */
    public ReferencedPoliciesType buildObject(String namespaceURI, String localName, String namespacePrefix) {
        return new ReferencedPoliciesTypeImpl(namespaceURI,localName,namespacePrefix);
    }

}
