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

import org.opensaml.saml2.metadata.EntitiesDescriptor;
import org.opensaml.xml.validation.ValidationException;
import org.opensaml.xml.validation.Validator;

/**
 * Checks {@link org.opensaml.saml2.metadata.EntitiesDescriptor} for Spec compliance.
 */
public class EntitiesDescriptorSpecValidator implements Validator<EntitiesDescriptor> {

    /** Constructor */
    public EntitiesDescriptorSpecValidator() {

    }

    /*
     * @see org.opensaml.xml.validation.Validator#validate(org.opensaml.xml.XMLObject)
     */
    public void validate(EntitiesDescriptor entitiesDescriptor) throws ValidationException {
        validateRoot(entitiesDescriptor);
    }

    /**
     * Checks that at least either Valid Until or Cache Duration is present when Entities Descriptor is root element.
     * 
     * @param entitiesDescriptor
     * @throws ValidationException
     */
    protected void validateRoot(EntitiesDescriptor entitiesDescriptor) throws ValidationException {
        if (entitiesDescriptor.getParent() == null && entitiesDescriptor.getValidUntil() == null
                && entitiesDescriptor.getCacheDuration() == null) {
            throw new ValidationException("Must have either ValidUntil or CacheDuration when is root element.");
        }
    }
}