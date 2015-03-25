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

package org.opensaml.soap.wstrust.impl;


import org.opensaml.core.xml.XMLObject;
import org.opensaml.core.xml.io.UnmarshallingException;
import org.opensaml.soap.wstrust.Participant;
import org.opensaml.soap.wstrust.Participants;
import org.opensaml.soap.wstrust.Primary;

/**
 * ParticipantsUnmarshaller.
 * 
 */
public class ParticipantsUnmarshaller extends AbstractWSTrustObjectUnmarshaller {

    /** {@inheritDoc} */
    protected void processChildElement(XMLObject parentXMLObject, XMLObject childXMLObject) 
            throws UnmarshallingException {
        Participants participants = (Participants) parentXMLObject;
        
        if (childXMLObject instanceof Primary) {
            participants.setPrimary((Primary) childXMLObject);
        } else if (childXMLObject instanceof Participant) {
            participants.getParticipants().add((Participant) childXMLObject);
        } else {
            participants.getUnknownXMLObjects().add(childXMLObject);
        }
    }

}
