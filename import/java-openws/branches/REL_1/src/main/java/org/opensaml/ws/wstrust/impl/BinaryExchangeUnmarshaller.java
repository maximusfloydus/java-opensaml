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

import org.opensaml.ws.wstrust.BinaryExchange;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.schema.impl.XSStringUnmarshaller;
import org.opensaml.xml.util.XMLHelper;
import org.w3c.dom.Attr;

/**
 * Unmarshaller for the &lt;wst:BinaryExchange&gt; element.
 * 
 * @see BinaryExchange
 * 
 */
public class BinaryExchangeUnmarshaller extends XSStringUnmarshaller {

    /** {@inheritDoc} */
    protected void processAttribute(XMLObject xmlObject, Attr attribute) throws UnmarshallingException {
        BinaryExchange binaryExchange = (BinaryExchange) xmlObject;
        
        String attrName = attribute.getLocalName();
        if (BinaryExchange.VALUE_TYPE_ATTRIB_NAME.equals(attrName)) {
            binaryExchange.setValueType(attribute.getValue());
        } else if (BinaryExchange.ENCODING_TYPE_ATTRIB_NAME.equals(attrName)) {
            binaryExchange.setEncodingType(attribute.getValue());
        } else {
            XMLHelper.unmarshallToAttributeMap(binaryExchange.getUnknownAttributes(), attribute);
        }
    }

}
