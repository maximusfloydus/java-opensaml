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

package org.opensaml.saml.saml1.core.validator;

import javax.xml.namespace.QName;

import org.opensaml.saml.common.BaseSAMLObjectValidatorTestCase;
import org.opensaml.saml.common.xml.SAMLConstants;
import org.opensaml.saml.saml1.core.Audience;
import org.opensaml.saml.saml1.core.validator.AudienceSpecValidator;
import org.opensaml.saml.saml1.core.validator.org;

/**
 * Test case for {@link org.opensaml.saml.saml1.core.validator.AudienceSpecValidator}.
 */
public class AudienceSpecTest extends BaseSAMLObjectValidatorTestCase {

    /** Constructor */
    public AudienceSpecTest() {
        super();
        targetQName = new QName(SAMLConstants.SAML1_NS, Audience.DEFAULT_ELEMENT_LOCAL_NAME, SAMLConstants.SAML1_PREFIX);
        validator = new AudienceSpecValidator();
    }

    /** {@inheritDoc} */
    protected void populateRequiredData() {
        super.populateRequiredData();
        
        Audience audience = (Audience) target;
        audience.setUri("http://shibboleth.middleware.edu/");
    }
    
    public void testMissingUri(){
        Audience audience = (Audience) target;
        
        audience.setUri(null);
        assertValidationFail("Audience was null, should raise a Validation Exception");
        
        audience.setUri("");
        assertValidationFail("Audience was empty, should raise a Validation Exception");
        
        audience.setUri("   ");
        assertValidationFail("Audience was just spaces, should raise a Validation Exception");
    }
}