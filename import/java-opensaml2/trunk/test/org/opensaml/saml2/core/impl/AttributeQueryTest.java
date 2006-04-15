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

import javax.xml.namespace.QName;

import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeQuery;

/**
 *
 */
public class AttributeQueryTest extends SubjectQueryTest {
    
    /** Expected number of Attribute child elements */
    private int expectedNumAttributes;

    /**
     * Constructor
     *
     */
    public AttributeQueryTest() {
        super();
        singleElementFile = "/data/org/opensaml/saml2/core/impl/AttributeQuery.xml";
        singleElementOptionalAttributesFile = "/data/org/opensaml/saml2/core/impl/AttributeQueryOptionalAttributes.xml";
        childElementsFile = "/data/org/opensaml/saml2/core/impl/AttributeQueryChildElements.xml";
    }
    
    
    
    /**
     * @see org.opensaml.saml2.core.impl.SubjectQueryTest#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        expectedNumAttributes = 4;
    }

    /**
     * @see org.opensaml.saml2.core.impl.SubjectQueryTest#testSingleElementMarshall()
     */
    public void testSingleElementMarshall() {
        QName qname = new QName(SAMLConstants.SAML20P_NS, AttributeQuery.DEFAULT_ELEMENT_LOCAL_NAME, SAMLConstants.SAML20P_PREFIX);
        AttributeQuery query = (AttributeQuery) buildXMLObject(qname);
        
        super.populateRequiredAttributes(query);
        
        assertEquals(expectedDOM, query);
    }
    
    

    /**
     * @see org.opensaml.common.SAMLObjectBaseTestCase#testSingleElementOptionalAttributesMarshall()
     */
    public void testSingleElementOptionalAttributesMarshall() {
        QName qname = new QName(SAMLConstants.SAML20P_NS, AttributeQuery.DEFAULT_ELEMENT_LOCAL_NAME, SAMLConstants.SAML20P_PREFIX);
        AttributeQuery query = (AttributeQuery) buildXMLObject(qname);
        
        super.populateRequiredAttributes(query);
        super.populateOptionalAttributes(query);
        
        assertEquals(expectedOptionalAttributesDOM, query);
    }



    /**
     * @see org.opensaml.common.SAMLObjectBaseTestCase#testChildElementsMarshall()
     */
    public void testChildElementsMarshall() {
        QName qname = new QName(SAMLConstants.SAML20P_NS, AttributeQuery.DEFAULT_ELEMENT_LOCAL_NAME, SAMLConstants.SAML20P_PREFIX);
        AttributeQuery query = (AttributeQuery) buildXMLObject(qname);
        
       populateChildElements(query);
       
       QName attributeQName = new QName(SAMLConstants.SAML20_NS, Attribute.DEFAULT_ELEMENT_LOCAL_NAME, SAMLConstants.SAML20_PREFIX);
       for (int i= 0; i<expectedNumAttributes; i++){
           query.getAttributes().add((Attribute) buildXMLObject(attributeQName));
       }
      
       assertEquals(expectedChildElementsDOM, query);
    }



    /**
     * @see org.opensaml.saml2.core.impl.SubjectQueryTest#testSingleElementUnmarshall()
     */
    public void testSingleElementUnmarshall() {
        AttributeQuery query = (AttributeQuery) unmarshallElement(singleElementFile);
        
        assertNotNull("AttributeQuery was null", query);
        super.helperTestSingleElementUnmarshall(query);

    }

    /**
     * @see org.opensaml.common.SAMLObjectBaseTestCase#testSingleElementOptionalAttributesUnmarshall()
     */
    public void testSingleElementOptionalAttributesUnmarshall() {
        AttributeQuery query = (AttributeQuery) unmarshallElement(singleElementOptionalAttributesFile);
        
        assertNotNull("AttributeQuery was null", query);
        super.helperTestSingleElementOptionalAttributesUnmarshall(query);
    }
    

    /**
     * @see org.opensaml.common.SAMLObjectBaseTestCase#testChildElementsUnmarshall()
     */
    public void testChildElementsUnmarshall() {
        AttributeQuery query = (AttributeQuery) unmarshallElement(childElementsFile);
        
        assertEquals("Attribute count", expectedNumAttributes, query.getAttributes().size());
        super.helperTestChildElementsUnmarshall(query);
    }
}