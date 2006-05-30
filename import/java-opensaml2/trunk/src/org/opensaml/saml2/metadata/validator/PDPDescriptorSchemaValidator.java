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

package org.opensaml.saml2.metadata.validator;

import org.opensaml.saml2.metadata.PDPDescriptor;
import org.opensaml.xml.validation.ValidationException;

/**
 * Checks {@link org.opensaml.saml2.metadata.PDPDescriptor} for Schema compliance.
 */
public class PDPDescriptorSchemaValidator extends RoleDescriptorSchemaValidator<PDPDescriptor> {

    /** Constructor */
    public PDPDescriptorSchemaValidator() {

    }

    /*
     * @see org.opensaml.xml.validation.Validator#validate(org.opensaml.xml.XMLObject)
     */
    public void validate(PDPDescriptor pdpDescriptor) throws ValidationException {
        super.validate(pdpDescriptor);
        validateAuthzServices(pdpDescriptor);
    }

    /**
     * Checks that one or more Authz Services are present.
     * 
     * @param pdpDescriptor
     * @throws ValidationException
     */
    protected void validateAuthzServices(PDPDescriptor pdpDescriptor) throws ValidationException {
        if (pdpDescriptor.getAuthzServices() == null || pdpDescriptor.getAuthzServices().size() == 0) {
            throw new ValidationException("Must have one or more AuthzServices.");
        }
    }
}