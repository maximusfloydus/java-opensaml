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

package org.opensaml.saml2.metadata.validator;

import javax.xml.namespace.QName;

import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml2.metadata.SPSSODescriptor;
import org.opensaml.saml2.metadata.AttributeConsumingService;
import org.opensaml.xml.validation.ValidationException;

/**
 * Test case for {@link org.opensaml.saml2.metadata.SPSSODescriptor}.
 */
public class SPSSODescriptorSchemaTest extends SSODescriptorSchemaTest {

    /** Constructor */
    public SPSSODescriptorSchemaTest() {
        targetQName = new QName(SAMLConstants.SAML20MD_NS, SPSSODescriptor.LOCAL_NAME,
                SAMLConstants.SAML20MD_PREFIX);
        validator = new SPSSODescriptorSchemaValidator();
    }

    /*
     * @see org.opensaml.common.SAMLObjectValidatorBaseTestCase#populateRequiredData()
     */
    protected void populateRequiredData() {
        super.populateRequiredData();
        SPSSODescriptor spssoDescriptor = (SPSSODescriptor) target;
        AttributeConsumingService attributeConsumingService = (AttributeConsumingService) buildXMLObject(new QName(SAMLConstants.SAML20MD_NS,
                AttributeConsumingService.LOCAL_NAME, SAMLConstants.SAML20MD_PREFIX));
        spssoDescriptor.getAttributeConsumingServices().add(attributeConsumingService);
    }

    /**
     * Tests for AttributeConsumingService failure.
     * 
     * @throws ValidationException
     */
    public void testAttributeConsumingServiceFailure() throws ValidationException {
        SPSSODescriptor spssoDescriptor = (SPSSODescriptor) target;

        spssoDescriptor.getAttributeConsumingServices().clear();
        assertValidationFail("AttributeConsumingService list was empty, should raise a Validation Exception.");
    }
}