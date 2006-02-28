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

import org.opensaml.common.impl.AbstractSAMLObjectUnmarshaller;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml2.core.NewID;
import org.opensaml.xml.XMLObject;

/**
 * A thread-safe Unmarshaller for {@link org.opensaml.saml2.core.NewID}
 * objects.
 */
public class NewIDUnmarshaller extends AbstractSAMLObjectUnmarshaller {

    /**
     * Constructor
     *
     */
    public NewIDUnmarshaller() {
        super(SAMLConstants.SAML20P_NS, NewID.LOCAL_NAME);
    }

    /**
     * @see org.opensaml.xml.io.AbstractXMLObjectUnmarshaller#processElementContent(org.opensaml.xml.XMLObject, java.lang.String)
     */
    protected void processElementContent(XMLObject samlObject, String elementContent) {
        NewID newID = (NewID) samlObject;
        
        newID.setNewID(elementContent);
    }
}