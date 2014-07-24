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

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.Assert;
import javax.xml.namespace.QName;

import org.opensaml.core.xml.XMLObjectProviderBaseTestCase;
import org.opensaml.saml.common.xml.SAMLConstants;
import org.opensaml.saml.saml2.metadata.OrganizationName;

/**
 * Test case for creating, marshalling, and unmarshalling
 * {@link org.opensaml.saml.saml2.metadata.OrganizationName}.
 */
public class OrganizationNameTest extends XMLObjectProviderBaseTestCase {
    
    /** Expected name. */
    protected String expectValue;
    /** Expected language. */
    protected String expectLang;

    
    /**
     * Constructor
     */
    public OrganizationNameTest() {
        singleElementFile = "/data/org/opensaml/saml/saml2/metadata/impl/OrganizationName.xml";
    }
    
    /** {@inheritDoc} */
    @BeforeMethod
    protected void setUp() throws Exception {
        expectValue = "MyOrg";
        expectLang = "Language";
    }

    /** {@inheritDoc} */
    @Test
    public void testSingleElementUnmarshall() {
        OrganizationName name = (OrganizationName) unmarshallElement(singleElementFile);
        
        Assert.assertEquals(name.getValue(), expectValue, "Name was not expected value");
        Assert.assertEquals(name.getXMLLang(), expectLang, "xml:lang was not expected value");
        name.hashCode();
    }

    /** {@inheritDoc} */
    @Test
    public void testSingleElementMarshall() {
        QName qname = new QName(SAMLConstants.SAML20MD_NS, OrganizationName.DEFAULT_ELEMENT_LOCAL_NAME, SAMLConstants.SAML20MD_PREFIX);
        OrganizationName name = (OrganizationName) buildXMLObject(qname);
        
        name.setValue(expectValue);
        name.setXMLLang(expectLang);
        assertXMLEquals(expectedDOM, name);
    }
}