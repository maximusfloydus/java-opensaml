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

package org.opensaml.common.impl;

import org.opensaml.common.SAMLObject;
import org.opensaml.common.SAMLObjectBuilder;
import org.opensaml.xml.AbstractXMLObjectBuilder;

/**
 * Base builder for {@link org.opensaml.common.SAMLObject}s.
 */
public abstract class AbstractSAMLObjectBuilder<SAMLObjectType extends SAMLObject> extends
        AbstractXMLObjectBuilder<SAMLObjectType> implements SAMLObjectBuilder<SAMLObjectType> {

    /**
     * Builds a SAMLObject using the default name and namespace information provided SAML specifications.
     * 
     * @return built SAMLObject
     */
    public abstract SAMLObjectType buildObject();
}