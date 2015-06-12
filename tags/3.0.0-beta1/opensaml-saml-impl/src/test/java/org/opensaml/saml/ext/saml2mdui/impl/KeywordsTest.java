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
package org.opensaml.saml.ext.saml2mdui.impl;

import org.testng.annotations.Test;
import org.testng.Assert;
import java.util.ArrayList;
import java.util.List;

import org.opensaml.core.xml.XMLObjectProviderBaseTestCase;
import org.opensaml.saml.ext.saml2mdui.Keywords;

/**
 * Test case for creating, marshalling, and unmarshalling
 * {@link org.opensaml.saml.saml2.metadata.OrganizationName}.
 */
public class KeywordsTest extends XMLObjectProviderBaseTestCase {
    
    /** Expected Keywords. */
    private final List<String> expectedWords;
    /** Expected Language.*/
    private final String expectedLang;
    
    /**
     * Constructor.
     */
    public KeywordsTest() {
        singleElementFile = "/data/org/opensaml/saml/ext/saml2mdui/Keywords.xml";
        String[] contents = {"This", "is", "a", "six", "element", "keyword"}; 
        expectedWords = new ArrayList(contents.length);
        for (String s : contents) {
            expectedWords.add(s);
        }
        expectedLang = "en";
    }
    
    /** {@inheritDoc} */
    @Test
    public void testSingleElementUnmarshall() {
        Keywords name = (Keywords) unmarshallElement(singleElementFile);
        
        Assert.assertEquals(name.getKeywords(), expectedWords, "Keyworks were not expected value");
        Assert.assertEquals(name.getXMLLang(), expectedLang, "Language was not expected value");
    }

    /** {@inheritDoc} */
    @Test
    public void testSingleElementMarshall() {
        Keywords keywords = (Keywords) buildXMLObject(Keywords.DEFAULT_ELEMENT_NAME);
        keywords.setXMLLang(expectedLang);
        keywords.setKeywords(expectedWords);

        assertXMLEquals(expectedDOM, keywords);
    }
}