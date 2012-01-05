/*
 * Licensed to the University Corporation for Advanced Internet Development, 
 * Inc. (UCAID) under one or more contributor license agreements.  See the 
 * NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The UCAID licenses this file to You under the Apache 
 * License, Version 2.0 (the "License"); you may not use this file except in 
 * compliance with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opensaml.ws.wssecurity.impl;

import net.shibboleth.utilities.java.support.xml.ElementSupport;

import org.opensaml.ws.wssecurity.AttributedDateTime;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.util.XMLObjectHelper;
import org.w3c.dom.Element;

import com.google.common.base.Strings;

/**
 * AttributedDateTimeMarshaller.
 * 
 */
public class AttributedDateTimeMarshaller extends AbstractWSSecurityObjectMarshaller {

    /** {@inheritDoc} */
    protected void marshallAttributes(XMLObject xmlObject, Element domElement) throws MarshallingException {
        AttributedDateTime dateTime = (AttributedDateTime) xmlObject;
        
        if (!Strings.isNullOrEmpty(dateTime.getWSUId())) {
            XMLObjectHelper.marshallAttribute(AttributedDateTime.WSU_ID_ATTR_NAME, dateTime.getWSUId(), domElement, true);
        }
        
        XMLObjectHelper.marshallAttributeMap(dateTime.getUnknownAttributes(), domElement);
        
    }

    /** {@inheritDoc} */
    protected void marshallElementContent(XMLObject xmlObject, Element domElement) throws MarshallingException {
        AttributedDateTime dateTime = (AttributedDateTime) xmlObject;
        ElementSupport.appendTextContent(domElement, dateTime.getValue());
    }
    
}
