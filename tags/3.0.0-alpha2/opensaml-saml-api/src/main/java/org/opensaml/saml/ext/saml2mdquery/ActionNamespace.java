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

package org.opensaml.saml.ext.saml2mdquery;

import javax.xml.namespace.QName;

import org.opensaml.core.xml.schema.XSURI;
import org.opensaml.saml.common.SAMLObject;
import org.opensaml.saml.common.xml.SAMLConstants;

/**
 * SAML 2.0 Metadata extension ActionNamespace
 */
public interface ActionNamespace extends XSURI, SAMLObject {

    /** Element local name */
    public final static String DEFAULT_ELEMENT_LOCAL_NAME = "ActionNamespace";

    /** Default element name */
    public final static QName DEFAULT_ELEMENT_NAME = new QName(SAMLConstants.SAML20MDQUERY_NS,
            DEFAULT_ELEMENT_LOCAL_NAME, SAMLConstants.SAML20MDQUERY_PREFIX);
}