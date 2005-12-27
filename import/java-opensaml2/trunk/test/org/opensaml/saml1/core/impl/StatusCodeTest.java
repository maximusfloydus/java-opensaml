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

package org.opensaml.saml1.core.impl;

import org.opensaml.common.IllegalAddException;
import org.opensaml.common.SAMLObjectBaseTestCase;
import org.opensaml.common.util.xml.ParserPoolManager;
import org.opensaml.saml1.core.StatusCode;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 * Test class for org.opensaml.saml1.core.StatusCode
 */
public class StatusCodeTest extends SAMLObjectBaseTestCase {

    /** A file with a Conditions with kids */

    private final String fullElementsFile;

    /** The expected result of a marshalled multiple element */

    private Document expectedFullDOM;
    
    /** The expected value for the value attribute */
    
    private final String value; 
    
    /** The expected value for the value attribute okf the child element */
    
    private final String childValue; 

    /**
     * Constructor
     *
     */
    public StatusCodeTest() {
        fullElementsFile = "/data/org/opensaml/saml1/FullStatusCode.xml";
        singleElementFile = "/data/org/opensaml/saml1/singleStatusCode.xml";
        singleElementOptionalAttributesFile = "/data/org/opensaml/saml1/singleStatusCode.xml";
        value = "samlp:Success";
        childValue = "samlp:VersionMismatch";
    }
    
    protected void setUp() throws Exception {
        super.setUp();

        ParserPoolManager ppMgr = ParserPoolManager.getInstance();

        expectedFullDOM = ppMgr.parse(new InputSource(SAMLObjectBaseTestCase.class
                .getResourceAsStream(fullElementsFile)));
    }

    @Override
    public void testSingleElementUnmarshall() {

        StatusCode code = (StatusCode) unmarshallElement(singleElementFile); 
        
        assertEquals("Single Element Value wrong", value, code.getValue());
    }

    @Override
    public void testSingleElementOptionalAttributesUnmarshall() {
       // Nothing to test
    }
    
    /*
     * Test an Response file with children
     */

    public void testFullElementsUnmarshall() {

        StatusCode code = (StatusCode) unmarshallElement(fullElementsFile);
        
        assertNotNull("Child StatusCode", code.getStatusCode());
    }

    @Override
    public void testSingleElementMarshall() {
        StatusCode code = new StatusCodeImpl();
        
        code.setValue(value);
        
        assertEquals(expectedDOM, code);
    }

    @Override
    public void testSingleElementOptionalAttributesMarshall() {
        // Nothing to test
    }

    public void testFullElementsMarshall() {

        StatusCode code = new StatusCodeImpl();
        
        code.setValue(value);
        
        try {
            code.setStatusCode(new StatusCodeImpl());
        } catch (IllegalAddException e) {
            fail("threw IllegalAddException");
        }
        code.getStatusCode().setValue(childValue);
        
        assertEquals(expectedFullDOM, code);
    }
}
