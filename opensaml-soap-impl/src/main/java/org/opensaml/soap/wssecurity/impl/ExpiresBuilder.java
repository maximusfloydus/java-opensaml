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

package org.opensaml.soap.wssecurity.impl;

import org.opensaml.soap.wssecurity.Expires;

/**
 * ExpiresBuilder.
 */
public class ExpiresBuilder extends AbstractWSSecurityObjectBuilder<Expires> {

    /*
     * (non-Javadoc)
     * 
     * @see org.opensaml.soap.wssecurity.impl.AbstractWSSecurityObjectBuilder#buildObject()
     */
    @Override
    public Expires buildObject() {
        return buildObject(Expires.ELEMENT_NAME);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.opensaml.xml.AbstractXMLObjectBuilder#buildObject(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @Override
    public Expires buildObject(String namespaceURI, String localName,
            String namespacePrefix) {
        return new ExpiresImpl(namespaceURI, localName, namespacePrefix);
    }

}
