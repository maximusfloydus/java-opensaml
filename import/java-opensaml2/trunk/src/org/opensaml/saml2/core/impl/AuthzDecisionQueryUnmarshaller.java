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

/**
 * 
 */
package org.opensaml.saml2.core.impl;

import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml2.core.Action;
import org.opensaml.saml2.core.AuthzDecisionQuery;
import org.opensaml.saml2.core.Evidence;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.UnmarshallingException;
import org.w3c.dom.Attr;

/**
 * A thread-safe Unmarshaller for {@link org.opensaml.saml2.core.AuthzDecisionQuery} objects.
 */
public class AuthzDecisionQueryUnmarshaller extends SubjectQueryUnmarshaller {

    /**
     * Constructor
     *
     */
    public AuthzDecisionQueryUnmarshaller() {
        super(SAMLConstants.SAML20P_NS, AuthzDecisionQuery.LOCAL_NAME);
    }

    /**
     * @see org.opensaml.xml.io.AbstractXMLObjectUnmarshaller#processAttribute(org.opensaml.xml.XMLObject, org.w3c.dom.Attr)
     */
    protected void processAttribute(XMLObject samlObject, Attr attribute) throws UnmarshallingException {
        AuthzDecisionQuery query = (AuthzDecisionQuery) samlObject;
        
        if (attribute.getLocalName().equals(AuthzDecisionQuery.RESOURCE_ATTRIB_NAME))
            query.setResource(attribute.getValue());
        else
            super.processAttribute(samlObject, attribute);
    }

    /**
     * @see org.opensaml.xml.io.AbstractXMLObjectUnmarshaller#processChildElement(org.opensaml.xml.XMLObject, org.opensaml.xml.XMLObject)
     */
    protected void processChildElement(XMLObject parentSAMLObject, XMLObject childSAMLObject) throws UnmarshallingException {
        AuthzDecisionQuery query = (AuthzDecisionQuery) parentSAMLObject;
        
        if (childSAMLObject instanceof Action)
            query.getActions().add((Action) childSAMLObject);
        else if (childSAMLObject instanceof Evidence)
            query.setEvidence((Evidence) childSAMLObject);
        else
            super.processChildElement(parentSAMLObject, childSAMLObject);
    }
}