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

package org.opensaml.saml2.metadata.impl;

import java.util.List;
import java.util.Map.Entry;

import javax.xml.namespace.QName;

import org.apache.log4j.Logger;
import org.joda.time.format.ISODateTimeFormat;
import org.opensaml.common.impl.AbstractSAMLObjectMarshaller;
import org.opensaml.saml2.common.CacheableSAMLObject;
import org.opensaml.saml2.common.TimeBoundSAMLObject;
import org.opensaml.saml2.metadata.RoleDescriptor;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.util.DatatypeHelper;
import org.opensaml.xml.util.XMLHelper;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;

/**
 * A thread safe Marshaller for {@link org.opensaml.saml2.metadata.RoleDescriptor} objects.
 */
public abstract class RoleDescriptorMarshaller extends AbstractSAMLObjectMarshaller {

    /**
     * Logger
     */
    private static Logger log = Logger.getLogger(RoleDescriptorMarshaller.class);

    /**
     * Constructor
     * 
     * @param targetNamespaceURI the namespaceURI of the SAMLObject this marshaller operates on
     * @param targetLocalName the local name of the SAMLObject this marshaller operates on
     */
    protected RoleDescriptorMarshaller(String targetNamespaceURI, String targetLocalName) {
        super(targetNamespaceURI, targetLocalName);
    }

    /** {@inheritDoc} */
    protected void marshallAttributes(XMLObject samlElement, Element domElement) throws MarshallingException {
        RoleDescriptor roleDescriptor = (RoleDescriptor) samlElement;

        // Set the ID attribute
        if (roleDescriptor.getID() != null) {
            if (log.isDebugEnabled()) {
                log.debug("Writing ID attribute to RoleDescriptor DOM element");
            }
            domElement.setAttributeNS(null, RoleDescriptor.ID_ATTRIB_NAME, roleDescriptor.getID());
        }

        // Set the validUntil attribute
        if (roleDescriptor.getValidUntil() != null) {
            if (log.isDebugEnabled()) {
                log.debug("Writting validUntil attribute to RoleDescriptor DOM element");
            }
            String validUntilStr = ISODateTimeFormat.dateTime().print(roleDescriptor.getValidUntil());
            domElement.setAttributeNS(null, TimeBoundSAMLObject.VALID_UNTIL_ATTRIB_NAME, validUntilStr);
        }

        // Set the cacheDuration attribute
        if (roleDescriptor.getCacheDuration() != null) {
            if (log.isDebugEnabled()) {
                log.debug("Writting cacheDuration attribute to EntitiesDescriptor DOM element");
            }
            String cacheDuration = DatatypeHelper.longToDuration(roleDescriptor.getCacheDuration());
            domElement.setAttributeNS(null, CacheableSAMLObject.CACHE_DURATION_ATTRIB_NAME, cacheDuration);
        }

        // Set the protocolSupportEnumeration attribute
        List<String> supportedProtocols = roleDescriptor.getSupportedProtocols();
        if (supportedProtocols != null && supportedProtocols.size() > 0) {
            if (log.isDebugEnabled()) {
                log.debug("Writting protocolSupportEnumberation attribute to RoleDescriptor DOM element");
            }

            StringBuilder builder = new StringBuilder();
            for (String protocol : supportedProtocols) {
                builder.append(protocol);
                builder.append(" ");
            }

            domElement.setAttributeNS(null, RoleDescriptor.PROTOCOL_ENUMERATION_ATTRIB_NAME, builder.toString().trim());
        }

        // Set errorURL attribute
        if (roleDescriptor.getErrorURL() != null) {
            if (log.isDebugEnabled()) {
                log.debug("Writting errorURL attribute to RoleDescriptor DOM element");
            }
            domElement.setAttributeNS(null, RoleDescriptor.ERROR_URL_ATTRIB_NAME, roleDescriptor.getErrorURL());
        }
        
        Attr attribute;
        for(Entry<QName, String> entry: roleDescriptor.getUnknownAttributes().entrySet()){
            attribute = XMLHelper.constructAttribute(domElement.getOwnerDocument(), entry.getKey());
            attribute.setValue(entry.getValue());
            domElement.setAttributeNode(attribute);
        }
    }
}