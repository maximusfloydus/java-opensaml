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

package org.opensaml.xacml.policy.impl;

import org.opensaml.xacml.XACMLConstants;
import org.opensaml.xacml.policy.VariableDefinitionType;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.AbstractXMLObjectUnmarshaller;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.util.DatatypeHelper;
import org.w3c.dom.Attr;

/**
 * Unmarshaller for {@link VariableDefinitionType}.
 */
public class VariableDefinitionTypeUnmarshaller extends AbstractXMLObjectUnmarshaller {


    /** Constructor. */
    public VariableDefinitionTypeUnmarshaller() {
        super(XACMLConstants.XACML20_NS,VariableDefinitionType.DEFAULT_ELEMENT_LOCAL_NAME);
    }
    
    /** {@inheritDoc} */
    protected void processAttribute(XMLObject xmlObject, Attr attribute) throws UnmarshallingException {
        
        if(attribute.getLocalName().equals(VariableDefinitionType.VARIABLE_ID_ATTRIB_NAME)){
            VariableDefinitionType variableDefinitionType = (VariableDefinitionType) xmlObject;
            variableDefinitionType.setVariableId(DatatypeHelper.safeTrimOrNullString(attribute.getValue()));
        }

    }

    /** {@inheritDoc} */
    protected void processChildElement(XMLObject parentXMLObject, XMLObject childXMLObject)
            throws UnmarshallingException {

    }

    /** {@inheritDoc} */
    protected void processElementContent(XMLObject xmlObject, String elementContent) {

    }
    
}
