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

package org.opensaml.soap.soap11.impl;

import org.opensaml.soap.common.SOAPObjectBuilder;
import org.opensaml.soap.soap11.Body;
import org.opensaml.soap.util.SOAPConstants;
import org.opensaml.xml.AbstractXMLObjectBuilder;

/**
 * Builder of {@link org.opensaml.soap.soap11.impl.BodyImpl} objects.
 */
public class BodyBuilder extends AbstractXMLObjectBuilder<Body> implements SOAPObjectBuilder<Body> {

    /**
     * Creates an envelope object with the default SOAP 1.1 namespace, prefix and "Body" as the element local name.
     * 
     * @return the build Envelope object
     */
    public Body buildObject(){
        return buildObject(SOAPConstants.SOAP11_NS, Body.DEFAULT_ELEMENT_LOCAL_NAME, SOAPConstants.SOAP11_PREFIX);
    }
    
    /** {@inheritDoc} */
    public Body buildObject(String namespaceURI, String localName, String namespacePrefix) {
        return new BodyImpl(namespaceURI, localName, namespacePrefix);
    }
}