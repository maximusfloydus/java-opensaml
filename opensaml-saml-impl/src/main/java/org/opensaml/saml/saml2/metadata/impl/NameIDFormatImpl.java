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

/**
 * 
 */

package org.opensaml.saml.saml2.metadata.impl;

import java.util.List;

import org.opensaml.core.xml.XMLObject;
import org.opensaml.saml.common.impl.AbstractSAMLObject;
import org.opensaml.saml.saml2.metadata.NameIDFormat;

/**
 * Concrete implementation of {@link org.opensaml.saml.saml2.metadata.NameIDFormat}.
 */
public class NameIDFormatImpl extends AbstractSAMLObject implements NameIDFormat {

    /** NameID format */
    private String format;

    /**
     * Constructor
     * 
     * @param namespaceURI
     * @param elementLocalName
     * @param namespacePrefix
     */
    protected NameIDFormatImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
    }

    /** {@inheritDoc} */
    public String getFormat() {
        return format;
    }

    /** {@inheritDoc} */
    public void setFormat(String format) {
        this.format = prepareForAssignment(this.format, format);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        return null;
    }
}