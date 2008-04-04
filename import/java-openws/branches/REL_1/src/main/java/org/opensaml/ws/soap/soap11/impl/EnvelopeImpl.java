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

package org.opensaml.ws.soap.soap11.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.opensaml.ws.soap.common.AbstractExtensibleSOAPObject;
import org.opensaml.ws.soap.soap11.Body;
import org.opensaml.ws.soap.soap11.Envelope;
import org.opensaml.ws.soap.soap11.Header;
import org.opensaml.xml.XMLObject;

/**
 * Concrete implementation of {@link org.opensaml.ws.soap.soap11.Envelope}.
 */
public class EnvelopeImpl extends AbstractExtensibleSOAPObject implements Envelope {

    /** SOAP header. */
    private Header header;

    /** SOAP body. */
    private Body body;

    /**
     * Constructor.
     * 
     * @param namespaceURI namespace of the element
     * @param elementLocalName name of the element
     * @param namespacePrefix namespace prefix of the element
     */
    protected EnvelopeImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
    }

    /** {@inheritDoc} */
    public Header getHeader() {
        return header;
    }

    /** {@inheritDoc} */
    public void setHeader(Header newHeader) {
        header = prepareForAssignment(header, newHeader);
    }

    /** {@inheritDoc} */
    public Body getBody() {
        return body;
    }

    /** {@inheritDoc} */
    public void setBody(Body newBody) {
        body = prepareForAssignment(body, newBody);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();

        children.add(header);
        children.add(body);
        children.addAll(super.getOrderedChildren());

        return Collections.unmodifiableList(children);
    }
}
