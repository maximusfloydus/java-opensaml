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

import org.opensaml.common.SAMLObject;
import org.opensaml.common.io.Marshaller;
import org.opensaml.common.io.MarshallingException;
import org.opensaml.common.io.impl.AbstractMarshaller;
import org.opensaml.saml1.core.Audience;
import org.w3c.dom.Element;

/**
 * A thread safe {@link org.opensaml.common.io.Marshaller} for {@link org.opensaml.saml1.core.Audience} objects.
 */
public class AudienceMarshaller extends AbstractMarshaller implements Marshaller {

    /**
     * Constructor
     */
    public AudienceMarshaller() {
        super(Audience.QNAME);
    }

    /*
     * @see org.opensaml.common.io.impl.AbstractMarshaller#marshallAttributes(org.opensaml.common.SAMLObject,
     *      org.w3c.dom.Element)
     */
    @Override
    protected void marshallAttributes(SAMLObject samlElement, Element domElement) throws MarshallingException {
        // No attributes - do nothing
    }

    /*
     * @see org.opensaml.common.io.impl.AbstractMarshaller#marshallElementContent(org.opensaml.common.SAMLObject,
     *      org.w3c.dom.Element)
     */
    @Override
    protected void marshallElementContent(SAMLObject samlObject, Element domElement) throws MarshallingException {
        Audience audience = (Audience) samlObject;

        domElement.setTextContent(audience.getUri());
    }
}
