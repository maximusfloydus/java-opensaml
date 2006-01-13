/*
 * Copyright [2006] [University Corporation for Advanced Internet Development, Inc.]
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

import java.util.Collection;
import java.util.List;

import org.opensaml.common.SAMLObject;
import org.opensaml.xml.IllegalAddException;

/**
 * SAML 2.0 Core Attribute
 */
public interface Attribute extends SAMLObject {

    /** Local name of the Attribute element */
    public static final String LOCAL_NAME = "Attribute";

    /** Name of the Name attribute */
    public static final String NAME_ATTTRIB_NAME = "Name";

    /** Name for the NameFormat attribute */
    public static final String NAME_FORMAT_ATTRIB_NAME = "NameFormat";

    /** Name of the FriendlyName attribute */
    public static final String FRIENDLY_NAME_ATTRIB_NAME = "FriendlyName";

    /**
     * Get the name of this attribute.
     * 
     * @return the name of this attribute
     */
    public String getName();

    /**
     * Sets the name of this attribute.
     * 
     * @param name the name of this attribute
     */
    public void setName(String name);

    /**
     * Get the name format of this attribute.
     * 
     * @return the name format of this attribute
     */
    public String getNameFormat();

    /**
     * Sets the name format of this attribute.
     * 
     * @param nameFormat the name format of this attribute
     */
    public void setNameFormat(String nameFormat);

    /**
     * Get the friendly name of this attribute.
     * 
     * @return the friendly name of this attribute
     */
    public String getFriendlyName();

    /**
     * Sets the friendly name of this attribute.
     * 
     * @param friendlyName the friendly name of this attribute
     */
    public void setFriendlyName(String friendlyName);

    /**
     * Gets the immutable list of attribute values for this attribute.
     * 
     * @return the list of attribute values for this attribute
     */
    public List<AttributeValue> getAttributeValues();

    /**
     * Adds an attribute value to the list of values for this attribute.
     * 
     * @param value the value to add
     * 
     * @throws IllegalAddException thrown if the given value is already the child of another SAMLObject
     */
    public void addAttributeValue(AttributeValue value) throws IllegalAddException;

    /**
     * Removes the value from the list of values for this attribute.
     * 
     * @param value the value to be removed
     */
    public void removeAttributeValue(AttributeValue value);

    /**
     * Removes the collection of values from the list of values for this attribute.
     * 
     * @param values the collection of values to be removed
     */
    public void removeAttributeValues(Collection<AttributeValue> values);

    /**
     * Removes all the values from the list of values for this attribute.
     */
    public void removeAllAttributeValues();
}