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

package org.opensaml.xmlsec.encryption.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.opensaml.core.xml.XMLObject;
import org.opensaml.xmlsec.encryption.CarriedKeyName;
import org.opensaml.xmlsec.encryption.EncryptedKey;
import org.opensaml.xmlsec.encryption.ReferenceList;

/**
 * Concrete implementation of {@link org.opensaml.xmlsec.encryption.EncryptedKey}.
 */
public class EncryptedKeyImpl extends EncryptedTypeImpl implements EncryptedKey {
    
    /** Recipient value. */
    private String recipient;
    
    /** CarriedKeyName value. */
    private CarriedKeyName carriedKeyName;
    
    /** ReferenceList value. */
    private ReferenceList referenceList;

    /**
     * Constructor.
     *
     * @param namespaceURI namespace URI
     * @param elementLocalName local name
     * @param namespacePrefix namespace prefix
     */
    protected EncryptedKeyImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
    }

    /** {@inheritDoc} */
    public String getRecipient() {
        return this.recipient;
    }

    /** {@inheritDoc} */
    public void setRecipient(String newRecipient) {
        this.recipient = prepareForAssignment(this.recipient, newRecipient);
    }

    /** {@inheritDoc} */
    public ReferenceList getReferenceList() {
        return this.referenceList;
    }

    /** {@inheritDoc} */
    public void setReferenceList(ReferenceList newReferenceList) {
        this.referenceList = prepareForAssignment(this.referenceList, newReferenceList);
    }

    /** {@inheritDoc} */
    public CarriedKeyName getCarriedKeyName() {
        return this.carriedKeyName;
    }

    /** {@inheritDoc} */
    public void setCarriedKeyName(CarriedKeyName newCarriedKeyName) {
        this.carriedKeyName = prepareForAssignment(this.carriedKeyName, newCarriedKeyName);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<>();
        
        if (super.getOrderedChildren() != null) {
            children.addAll(super.getOrderedChildren());
        }
       
        if (referenceList != null) {
            children.add(referenceList);
        }
        if (carriedKeyName != null) {
            children.add(carriedKeyName);
        }
        
        if (children.size() == 0) {
            return null;
        }
        
        return Collections.unmodifiableList(children);
    }

}
