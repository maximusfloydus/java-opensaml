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

package org.opensaml.saml2.core;

import java.util.List;

import javax.xml.namespace.QName;

import org.joda.time.DateTime;
import org.opensaml.common.SAMLObject;
import org.opensaml.common.xml.SAMLConstants;

/**
 * SAML 2.0 Core Conditions
 */
public interface Conditions extends SAMLObject {

    /** Element local name */
    public final static String DEFAULT_ELEMENT_LOCAL_NAME = "Conditions";
    
    /** Default element name */
    public final static QName DEFUALT_ELEMENT_NAME = new QName(SAMLConstants.SAML20_NS, DEFAULT_ELEMENT_LOCAL_NAME, SAMLConstants.SAML20_PREFIX);
    
    /** Local name of the XSI type */
    public final static String TYPE_LOCAL_NAME = "ConditionsType"; 
        
    /** QName of the XSI type */
    public final static QName TYPE_NAME = new QName(SAMLConstants.SAML20_NS, TYPE_LOCAL_NAME, SAMLConstants.SAML20_PREFIX);

    /** NotBefore attribute name */
    public final static String NOT_BEFORE_ATTRIB_NAME = "NotBefore";

    /** NotOnOrAfter attribute name */
    public final static String NOT_ON_OR_AFTER_ATTRIB_NAME = "NotOnOrAfter";

    /**
     * Get the date/time before which the assertion is invalid.
     * 
     * @return the date/time before which the assertion is invalid
     */
    public DateTime getNotBefore();

    /**
     * Sets the date/time before which the assertion is invalid.
     * 
     * @param newNotBefore the date/time before which the assertion is invalid
     */
    public void setNotBefore(DateTime newNotBefore);

    /**
     * Gets the date/time on, or after, which the assertion is invalid.
     * 
     * @return the date/time on, or after, which the assertion is invalid
     */
    public DateTime getNotOnOrAfter();

    /**
     * Sets the date/time on, or after, which the assertion is invalid.
     * 
     * @param newNotOnOrAfter the date/time on, or after, which the assertion is invalid
     */
    public void setNotOnOrAfter(DateTime newNotOnOrAfter);

    /**
     * Gets all the conditions on the assertion.
     * 
     * @return all the conditions on the assertion
     */
    public List<Condition> getConditions();

    /**
     * Gets the audience restriction conditions for the assertion.
     * 
     * @return the audience restriction conditions for the assertion
     */
    public List<AudienceRestriction> getAudienceRestrictions();

    /**
     * Gets the OneTimeUse condition for the assertion.
     * 
     * @return the OneTimeUse condition for the assertion
     */
    public OneTimeUse getOneTimeUse();

    /**
     * Gets the ProxyRestriction condition for the assertion.
     * 
     * @return the ProxyRestriction condition for the assertion
     */
    public ProxyRestriction getProxyRestriction();
}