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
package org.opensaml.saml1.core.impl;


import org.opensaml.common.IllegalAddException;
import org.opensaml.common.SAMLConfig;
import org.opensaml.common.SAMLObject;
import org.opensaml.common.io.UnknownAttributeException;
import org.opensaml.common.io.UnknownElementException;
import org.opensaml.common.io.Unmarshaller;
import org.opensaml.common.io.UnmarshallingException;
import org.opensaml.common.io.impl.AbstractUnmarshaller;
import org.opensaml.saml1.core.Status;
import org.opensaml.saml1.core.StatusCode;
import org.opensaml.saml1.core.StatusMessage;

/**
 * A thread-safe {@link org.opensaml.common.io.Unmarshaller} for {@link org.opensaml.saml1.core.Status} objects.
 */
public class StatusUnmarshaller extends AbstractUnmarshaller implements Unmarshaller {

    /**
     * Constructor
     */
    
    public StatusUnmarshaller() {
        super(Status.QNAME);

    }

    /*
     * @see org.opensaml.common.io.impl.AbstractUnmarshaller#processChildElement(org.opensaml.common.SAMLObject, org.opensaml.common.SAMLObject)
     */
    @Override
    protected void processChildElement(SAMLObject parentElement, SAMLObject childElement)
            throws UnmarshallingException, UnknownElementException {
        
        Status status = (Status) parentElement;
        
        try {
            if (childElement instanceof StatusCode) {
            
                status.setStatusCode((StatusCode)childElement);
        
            } else if (childElement instanceof StatusMessage) {
                
                status.setStatusMessage((StatusMessage) childElement);
                
            } else if (childElement instanceof SAMLObject) {
                
                //
                // TODO - more magicke
                //
                
                status.setStatusDetail(childElement);
            
            } else { 
                if(!SAMLConfig.ignoreUnknownElements()){
                    throw new UnknownElementException(childElement.getElementQName() + " is not a supported element for Response objects");
                }
            }
        } catch (IllegalAddException e) {
            throw new UnknownAttributeException(e);
        }
    }

    /*
     * @see org.opensaml.common.io.impl.AbstractUnmarshaller#processAttribute(org.opensaml.common.SAMLObject, java.lang.String, java.lang.String)
     */
    @Override
    protected void processAttribute(SAMLObject samlElement, String attributeName, String attributeValue)
            throws UnmarshallingException, UnknownAttributeException {

        if(!SAMLConfig.ignoreUnknownAttributes()){
            throw new UnknownAttributeException(attributeName + " is not a supported attributed for Status objects");
        }
    }

}
