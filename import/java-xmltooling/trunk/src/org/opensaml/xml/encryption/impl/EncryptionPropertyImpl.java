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

package org.opensaml.xml.encryption.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.opensaml.xml.XMLObject;
import org.opensaml.xml.util.AttributeMap;
import org.opensaml.xml.util.XMLObjectChildrenList;
import org.opensaml.xml.validation.AbstractValidatingXMLObject;

/**
 * Concrete implementation of {@link org.opensaml.xml.encryption.EncryptionProperty}
 */
public class EncryptionPropertyImpl extends AbstractValidatingXMLObject implements
        org.opensaml.xml.encryption.EncryptionProperty {
    
    /** Target attribute value */
    private String target;
    
    /** Id attribute value */
    private String id;
    
    /** Child elements from the <any> content model */
    private final XMLObjectChildrenList unknownChildren;
    
    /** "anyAttribute" attributes */
    private final AttributeMap unknownAttributes;

    /**
     * Constructor
     *
     * @param namespaceURI
     * @param elementLocalName
     * @param namespacePrefix
     */
    protected EncryptionPropertyImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        unknownChildren = new XMLObjectChildrenList<XMLObject>(this);
        unknownAttributes =  new AttributeMap(this);
    }

    /** {@inheritDoc} */
    public String getTarget() {
        return this.target;
    }

    /** {@inheritDoc} */
    public void setTarget(String newTarget) {
        this.target = prepareForAssignment(this.target, newTarget);
    }

    /** {@inheritDoc} */
    public String getID() {
        return this.id;
    }

    /** {@inheritDoc} */
    public void setID(String newID) {
        this.id = prepareForAssignment(this.id, newID);
    }

    /** {@inheritDoc} */
    public AttributeMap getUnknownAttributes() {
        return unknownAttributes;
    }

    /** {@inheritDoc} */
    public List<XMLObject> getUnknownXMLObjects() {
        return (List<XMLObject>) unknownChildren;
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();
        
        children.addAll((List<XMLObject>) unknownChildren);
        
        if (children.size() == 0) {
            return null;
        }
        
        return Collections.unmodifiableList(children);
    }

}
