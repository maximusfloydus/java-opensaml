/*
 * Copyright 2008 Members of the EGEE Collaboration.
 * Copyright 2008 University Corporation for Advanced Internet Development, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.opensaml.ws.wstrust.impl;


import org.opensaml.ws.wstrust.Code;
import org.opensaml.ws.wstrust.Reason;
import org.opensaml.ws.wstrust.Status;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.UnmarshallingException;

/**
 * Unmarshaller for the &lt;wst:Status&gt; element.
 * 
 * @see Status
 * 
 * @author Valery Tschopp &lt;tschopp@switch.ch&gt;
 * @version $Revision$
 */
public class StatusUnmarshaller extends AbstractWSTrustObjectUnmarshaller {

    /**
     * Default constructor.
     * <p>
     * {@inheritDoc}
     */
    public StatusUnmarshaller() {
        super();
    }

    /**
     * Unmarshalls the &lt;wst:Code&gt; and &lt;wst:Reason&gt; child elements.
     * <p>
     * {@inheritDoc}
     */
    @Override
    protected void processChildElement(XMLObject parentXMLObject,
            XMLObject childXMLObject) throws UnmarshallingException {
        Status container= (Status) parentXMLObject;
        if (childXMLObject instanceof Code) {
            container.setCode((Code) childXMLObject);
        }
        else if (childXMLObject instanceof Reason) {
            container.setReason((Reason) childXMLObject);
        }
        else {
            super.processChildElement(parentXMLObject, childXMLObject);
        }
    }

}
