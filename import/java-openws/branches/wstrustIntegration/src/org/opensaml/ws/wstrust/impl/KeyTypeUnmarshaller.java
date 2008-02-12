/*
 * Copyright 2008 Members of the EGEE Collaboration.
 * Copyright 2008 University Corporation for Advanced Internet Development, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.opensaml.ws.wstrust.impl;


import org.opensaml.ws.wstrust.KeyType;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.schema.XSURI;

/**
 * Unmarshaller for the &lt;wst:KeyType&gt; element.
 * 
 * @see KeyType
 * 
 * @author Valery Tschopp &lt;tschopp@switch.ch&gt;
 * @version $Revision$
 */
public class KeyTypeUnmarshaller extends AbstractWSTrustObjectUnmarshaller {

    /**
     * Default constructor.
     * <p>
     * {@inheritDoc}
     */
    public KeyTypeUnmarshaller() {
        super(KeyType.ELEMENT_NAME.getNamespaceURI(),
              KeyType.ELEMENT_NAME.getLocalPart());
    }

    /**
     * Unmarshalls the &lt;wst:KeyType&gt; element content.
     * <p>
     * {@inheritDoc}
     */
    @Override
    protected void processElementContent(XMLObject xmlObject,
            String elementContent) {
        if (elementContent != null) {
            XSURI xsUri= (XSURI) xmlObject;
            xsUri.setValue(elementContent);
        }
    }

}
