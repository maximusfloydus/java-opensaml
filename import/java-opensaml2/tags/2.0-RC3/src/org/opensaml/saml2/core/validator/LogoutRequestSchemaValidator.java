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
package org.opensaml.saml2.core.validator;

import org.opensaml.saml2.core.LogoutRequest;
import org.opensaml.xml.validation.ValidationException;

/**
 * Checks {@link org.opensaml.saml2.core.LogoutRequest} for Schema compliance.
 */
public class LogoutRequestSchemaValidator extends RequestAbstractTypeSchemaValidator<LogoutRequest> {

    /**
     * Constructor
     *
     */
    public LogoutRequestSchemaValidator() {
        super();
    }

    /** {@inheritDoc} */
    public void validate(LogoutRequest request) throws ValidationException {
        super.validate(request);
        validateIdentifiers(request);
    }

    /**
     * Validate the Identifier child types (BaseID, NameID, EncryptedID).
     * 
     * @param request
     * @throws ValidationException
     */
    protected void validateIdentifiers(LogoutRequest request) throws ValidationException {
        if (request.getBaseID() == null && request.getNameID() == null) {
            throw new ValidationException("Either NameID or BaseID child is required");
        }
        
        if (request.getBaseID() != null && request.getNameID() != null) {
            throw new ValidationException("NameID and BaseID are mutually exclusive");
        }
    }
    
    // TODO EncryptedID pending encryption implementation.
    
}
