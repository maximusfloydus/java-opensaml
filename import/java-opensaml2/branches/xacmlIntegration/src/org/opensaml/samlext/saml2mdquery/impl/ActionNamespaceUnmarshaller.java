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

import org.opensaml.common.impl.AbstractSAMLObjectUnmarshaller;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.samlext.saml2mdquery.ActionNamespace;
import org.opensaml.xml.XMLObject;

/**
 * Unmarshaller for {@link ActionNamespace} objects.
 */
public class ActionNamespaceUnmarshaller extends AbstractSAMLObjectUnmarshaller {

    /** Constructor */
    public ActionNamespaceUnmarshaller() {
        super(SAMLConstants.SAML20MDQUERY_NS, ActionNamespace.DEFAULT_ELEMENT_LOCAL_NAME);
    }
    
    /** {@inheritDoc} */
    protected void processElementContent(XMLObject samlObject, String elementContent) {
        ActionNamespace actionNamespace = (ActionNamespace) samlObject;
        
        actionNamespace.setValue(elementContent);
    }
}
