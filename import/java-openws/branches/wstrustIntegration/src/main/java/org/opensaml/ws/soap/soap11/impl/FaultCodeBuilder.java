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

package org.opensaml.ws.soap.soap11.impl;

import org.opensaml.ws.soap.common.SOAPObjectBuilder;
import org.opensaml.ws.soap.soap11.FaultCode;
import org.opensaml.xml.AbstractXMLObjectBuilder;

/**
 * Builder of {@link org.opensaml.ws.soap.soap11.impl.FaultCodeImpl} objects.
 */
public class FaultCodeBuilder extends AbstractXMLObjectBuilder<FaultCode> implements SOAPObjectBuilder<FaultCode> {

    /** {@inheritDoc} */
    public FaultCode buildObject(String namespaceURI, String localName, String namespacePrefix) {
        return new FaultCodeImpl(namespaceURI, localName, namespacePrefix);
    }

    /** {@inheritDoc} */
    public FaultCode buildObject() {
        return buildObject(FaultCode.DEFAULT_ELEMENT_NAME);
    }
}