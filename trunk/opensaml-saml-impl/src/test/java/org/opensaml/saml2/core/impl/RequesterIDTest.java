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

import org.opensaml.common.BaseSAMLObjectProviderTestCase;
import org.opensaml.saml2.core.RequesterID;

/**
 * Test case for creating, marshalling, and unmarshalling
 * {@link org.opensaml.saml2.core.impl.RequesterIDImpl}.
 */
public class RequesterIDTest extends BaseSAMLObjectProviderTestCase {
    
    /** Expected element content*/
    private String expectedRequesterID;

    /**
     * Constructor
     */
    public RequesterIDTest() {
        super();
        
       singleElementFile = "/data/org/opensaml/saml2/core/impl/RequesterID.xml";
    }

    /** {@inheritDoc} */
    protected void setUp() throws Exception {
        super.setUp();
        expectedRequesterID = "urn:string:requester";
    }

    /** {@inheritDoc} */
    public void testSingleElementUnmarshall() {
        RequesterID reqID = (RequesterID) unmarshallElement(singleElementFile);
        
       assertEquals("Unmarshalled requester ID was not the expected value", expectedRequesterID, reqID.getRequesterID()); 
    }

    /** {@inheritDoc} */
    public void testSingleElementMarshall() {
        RequesterID reqID = (RequesterID) buildXMLObject(RequesterID.DEFAULT_ELEMENT_NAME);

        reqID.setRequesterID(expectedRequesterID);
        
        assertEquals(expectedDOM, reqID);
    }
}