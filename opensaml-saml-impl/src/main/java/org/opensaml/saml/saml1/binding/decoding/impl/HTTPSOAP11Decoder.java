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

package org.opensaml.saml.saml1.binding.decoding.impl;

import org.opensaml.messaging.context.MessageContext;
import org.opensaml.messaging.decoder.MessageDecodingException;
import org.opensaml.saml.common.SAMLObject;
import org.opensaml.saml.common.binding.decoding.SAMLMessageDecoder;
import org.opensaml.saml.common.binding.impl.SAMLSOAPDecoderBodyHandler;
import org.opensaml.saml.common.messaging.context.SAMLBindingContext;
import org.opensaml.saml.common.xml.SAMLConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SAML 1.1 HTTP SOAP 1.1 binding decoder.
 */
public class HTTPSOAP11Decoder extends org.opensaml.soap.soap11.decoder.http.impl.HTTPSOAP11Decoder<SAMLObject> 
        implements SAMLMessageDecoder {
    
    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(HTTPSOAP11Decoder.class);

    /**
     * Constructor.
     */
    public HTTPSOAP11Decoder() {
        super();
        setBodyHandler(new SAMLSOAPDecoderBodyHandler());
    }

    /** {@inheritDoc} */
    public String getBindingURI() {
        return SAMLConstants.SAML1_SOAP11_BINDING_URI;
    }

    /** {@inheritDoc} */
    protected void doDecode() throws MessageDecodingException {
        super.doDecode();
        
        populateBindingContext(getMessageContext());
        
        SAMLObject samlMessage = getMessageContext().getMessage();
        log.debug("Decoded SOAP messaged which included SAML message of type {}", samlMessage.getElementQName());
    }
    
    /**
     * Populate the context which carries information specific to this binding.
     * 
     * @param messageContext the current message context
     */
    protected void populateBindingContext(MessageContext<SAMLObject> messageContext) {
        SAMLBindingContext bindingContext = messageContext.getSubcontext(SAMLBindingContext.class, true);
        bindingContext.setBindingUri(getBindingURI());
        bindingContext.setHasBindingSignature(false);
        bindingContext.setIntendedDestinationEndpointURIRequired(false);
    }

}