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

import org.joda.time.DateTime;
import org.opensaml.common.SAMLObject;

/**
 * SAML 2.0 Core SubjectConfirmationData
 */
public interface SubjectConfirmationData extends SAMLObject {
    
    /** Element local name */
    public final static String LOCAL_NAME = "SubjectConfirmationData";
    
    /** NotBefore attribute name */
    public final static String NOT_BEFORE_ATTRIB_NAME = "NotBefore";
    
    /** NotOnOrAfter attribute name */
    public final static String NOT_ON_OR_AFTER_ATTRIB_NAME = "NotOnOrAfter";
    
    /** Recipient attribute name */
    public final static String RECIPIENT_ATTRIB_NAME = "Recipient";

    /**
     * Gets the time before which this subject is not valid.
     * 
     * @return the time before which this subject is not valid
     */
    public DateTime getNotBefore();

    /**
     * Sets the time before which this subject is not valid.
     * 
     * @param newNotBefore the time before which this subject is not valid
     */
    public void setNotBefore(DateTime newNotBefore);

    /**
     * Gets the time at, or after, which this subject is not valid.
     * 
     * @return the time at, or after, which this subject is not valid
     */
    public DateTime getNotOnOrAfter();

    /**
     * Sets the time at, or after, which this subject is not valid.
     * 
     * @param newNotOnOrAfter the time at, or after, which this subject is not valid
     */
    public void setNotOnOrAfter(DateTime newNotOnOrAfter);

    /**
     * Gets the recipient of this subject.
     * 
     * @return the recipient of this subject
     */
    public String getRecipient();

    /**
     * Sets the recipient of this subject.
     * 
     * @param newRecipient the recipient of this subject
     */
    public void setRecipient(String newRecipient);

    /**
     * Gets the message ID this is in response to.
     * 
     * @return the message ID this is in response to
     */
    public String getInResponseTo();

    /**
     * Sets the message ID this is in response to.
     * 
     * @param newInResponseTo the message ID this is in response to
     */
    public void setInResponseTo(String newInResponseTo);

    /**
     * Gets the IP address to which this information may be pressented.
     * 
     * @return the IP address to which this information may be pressented
     */
    public String getAddress();

    /**
     * Sets the IP address to which this information may be pressented.
     * 
     * @param newAddress the IP address to which this information may be pressented
     */
    public void setAddress(String newAddress);
}