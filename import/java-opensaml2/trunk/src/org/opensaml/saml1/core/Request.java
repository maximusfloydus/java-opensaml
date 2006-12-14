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

package org.opensaml.saml1.core;

import java.util.List;

import javax.xml.namespace.QName;

import org.opensaml.common.xml.SAMLConstants;

/**
 * This interface defines how the SAML1 <code> Request </code> objects behave.
 */
public interface Request extends RequestAbstractType {

    /** Element name, no namespace. */
    public final static String DEFAULT_ELEMENT_LOCAL_NAME = "Request";
    
    /** Default element name */
    public final static QName DEFAULT_ELEMENT_NAME = new QName(SAMLConstants.SAML1P_NS, DEFAULT_ELEMENT_LOCAL_NAME, SAMLConstants.SAML1P_PREFIX);
    
    /** Local name of the XSI type */
    public final static String TYPE_LOCAL_NAME = "RequestType"; 
        
    /** QName of the XSI type */
    public final static QName TYPE_NAME = new QName(SAMLConstants.SAML1P_NS, TYPE_LOCAL_NAME, SAMLConstants.SAML1P_PREFIX);

    /* 
     * A bit odd this, it s a choice so only one of these will return any value
     */
    
    /** Get the request Query, SubjectQuery, AuthenticationQuery, AttributeQuery, or AuthorizationDecisionQuery */ 
    public Query getQuery();
    
    /** Get the request SubjectQuery, AuthenticationQuery, AttributeQuery, or AuthorizationDecisionQuery */ 
    public SubjectQuery getSubjectQuery();
    
    /** Get the query AuthenticationQuery */ 
    public AuthenticationQuery getAuthenticationQuery();
    
    /** Get the request AttributeQuery */ 
    public AttributeQuery getAttributeQuery();
    
    /** Get the request AuthorizationDecisionQuery */ 
    public AuthorizationDecisionQuery getAuthorizationDecisionQuery();
    
    /** Set the query (Query, SubjectQuery, AuthenticationQuery, AttributeQuery, AuthorizationDecisioonQuery 
     */ 
    public void setQuery(Query query) throws IllegalArgumentException;
    
    /** Get the lists of AssertionIDReferences */
    public List <AssertionIDReference> getAssertionIDReferences();
    
    /** Get the lists of */
    public List <AssertionArtifact> getAssertionArtifacts();
}
