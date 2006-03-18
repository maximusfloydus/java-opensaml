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
package org.opensaml.common;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import org.opensaml.xml.XMLObject;
import org.opensaml.xml.validation.ValidationException;
import org.opensaml.xml.validation.Validator;

/**
 * Base test case for all OpenSAML tests that test the {@link org.opensaml.xml.validation.Validator}'s
 * that validate SAML objects.
 */
public abstract class SAMLObjectValidatorBaseTestCase extends SAMLObjectTestCaseConfigInitializer {
    
    /** The primary XMLObject which will be the target of a given test run */
    protected XMLObject target;

    /** QName of the object to be tested */
    protected QName targetQName;

    /** Validator for the type corresponding to the test target */
    protected Validator validator;

    /** Context for the object construction */
    protected Map<String, Object> context;
    /**
    /**
     * Constructor
     *
     */
    public SAMLObjectValidatorBaseTestCase() {
        super();
        context = new HashMap<String, Object>();
    }

    /*
     * @see org.opensaml.common.SAMLObjectTestCaseConfigInitializer#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        
        if (targetQName == null)
            throw new Exception("targetQName was null");
        
        if (validator == null)
            throw new Exception("validator was null");
        //
        // TODO -replace with one with a context when the 
        target = buildXMLObject(targetQName);
        populateRequiredData();
    }

    /*
     * @see org.opensaml.common.SAMLObjectTestCaseConfigInitializer#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    /**
     *  Subclasses should override to populate required elements and attributes
     */
    protected void populateRequiredData() {
        
    }
    
    /**
     * Asserts that the validation of default test XMLObject target 
     * was successful, as expected.
     * 
     * @param message
     */
    protected void assertValidationPass(String message) {
        assertValidationPass(message, target);
    }
    
    /**
     * Asserts that the validation of the specified XMLObject target 
     * was successful, as expected.
     * 
     * @param message
     * @param validateTarget
     */
    protected void assertValidationPass(String message, XMLObject validateTarget) {
       try {
           validator.validate(validateTarget);
       } catch (ValidationException e) {
           fail(message + " : Expected success, but validation failure raised ValidationException: " + e.getMessage());
       }
    }
    
    /**
     * Asserts that the validation of the default test XMLObject target 
     * failed, as expected.
     * 
     * @param message
     */
    protected void assertValidationFail(String message) {
        assertValidationFail(message, target);
    }
    
    /**
     * Asserts that the validation of the specified XMLObject target 
     * failed, as expected.
     * 
     * @param message
     * @param validateTarget
     */
    protected void assertValidationFail(String message, XMLObject validateTarget) {
       try {
           validator.validate(validateTarget);
           fail(message + " : Validation success, expected failure to raise ValidationException");
       } catch (ValidationException e) {
       }
    }
    
    /**
     *  Tests the expected proper validation case.
     */
    public void testProperValidation() {
        assertValidationPass("SAML object was valid");
    } 

}
