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

package org.opensaml.xmlsec.signature.impl;

import org.opensaml.core.xml.AbstractXMLObjectBuilder;
import org.opensaml.xmlsec.signature.ECKeyValue;
import org.opensaml.xmlsec.signature.XMLSignatureBuilder;
import org.opensaml.xmlsec.signature.support.SignatureConstants;

/**
 * Builder of {@link ECKeyValue}.
 */
public class ECKeyValueBuilder extends AbstractXMLObjectBuilder<ECKeyValue>
    implements XMLSignatureBuilder<ECKeyValue> {

    /**
     * Constructor.
     */
    public ECKeyValueBuilder() {
    }

    /** {@inheritDoc} */
    public ECKeyValue buildObject(String namespaceURI, String localName, String namespacePrefix) {
        return new ECKeyValueImpl(namespaceURI, localName, namespacePrefix);
    }

    /** {@inheritDoc} */
    public ECKeyValue buildObject() {
        return buildObject(SignatureConstants.XMLSIG11_NS, ECKeyValue.DEFAULT_ELEMENT_LOCAL_NAME,
                SignatureConstants.XMLSIG11_PREFIX);
    }

}