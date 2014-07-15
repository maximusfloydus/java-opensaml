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

package org.opensaml.saml.saml2.binding.security.impl;

import javax.xml.namespace.QName;

import net.shibboleth.utilities.java.support.component.ComponentInitializationException;

import org.opensaml.core.OpenSAMLInitBaseTestCase;
import org.opensaml.core.xml.config.XMLObjectProviderRegistrySupport;
import org.opensaml.messaging.context.MessageContext;
import org.opensaml.messaging.handler.MessageHandlerException;
import org.opensaml.saml.common.messaging.context.ChannelBindingsContext;
import org.opensaml.saml.common.xml.SAMLConstants;
import org.opensaml.saml.ext.saml2cb.ChannelBindings;
import org.opensaml.saml.saml1.profile.SAML1ActionTestingSupport;
import org.opensaml.saml.saml2.common.Extensions;
import org.opensaml.saml.saml2.core.AuthnRequest;
import org.opensaml.saml.saml2.profile.SAML2ActionTestingSupport;
import org.testng.Assert;
import org.testng.annotations.Test;

/** {@link ExtractChannelBindingsExtensionsHandler} unit test. */
public class ExtractChannelBindingsExtensionsHandlerTest extends OpenSAMLInitBaseTestCase {
    
    /** Test that the handler returns nothing on a missing message. */
    @Test public void testMissingMessage() throws MessageHandlerException, ComponentInitializationException {
        final MessageContext messageCtx = new MessageContext();
        
        final ExtractChannelBindingsExtensionsHandler handler = new ExtractChannelBindingsExtensionsHandler();
        handler.initialize();
        
        handler.invoke(messageCtx);
        Assert.assertNull(messageCtx.getSubcontext(ChannelBindingsContext.class));
        
        messageCtx.setMessage(SAML1ActionTestingSupport.buildResponse());
        handler.invoke(messageCtx);
        Assert.assertNull(messageCtx.getSubcontext(ChannelBindingsContext.class));
    }

    /** Test that the handler does nothing when no extensions exist. */
    @Test public void testNoExtensions() throws MessageHandlerException, ComponentInitializationException {
        final MessageContext<AuthnRequest> messageCtx = new MessageContext<>();
        messageCtx.setMessage(SAML2ActionTestingSupport.buildAuthnRequest());
        
        final ExtractChannelBindingsExtensionsHandler handler = new ExtractChannelBindingsExtensionsHandler();
        handler.initialize();
        
        handler.invoke(messageCtx);
        Assert.assertNull(messageCtx.getSubcontext(ChannelBindingsContext.class));
    }
    
    /** Test that the handler works. */
    @Test public void testSuccess() throws MessageHandlerException, ComponentInitializationException {
        final QName extQName = new QName(SAMLConstants.SAML20P_NS, Extensions.LOCAL_NAME);
        final Extensions ext = XMLObjectProviderRegistrySupport.getBuilderFactory().<Extensions>getBuilderOrThrow(
                extQName).buildObject(extQName);

        final MessageContext<AuthnRequest> messageCtx = new MessageContext<>();
        messageCtx.setMessage(SAML2ActionTestingSupport.buildAuthnRequest());
        messageCtx.getMessage().setExtensions(ext);
        
        final ChannelBindings cb = XMLObjectProviderRegistrySupport.getBuilderFactory().<ChannelBindings>getBuilderOrThrow(
                ChannelBindings.DEFAULT_ELEMENT_NAME).buildObject(ChannelBindings.DEFAULT_ELEMENT_NAME);
        cb.setValue("foo");
        ext.getUnknownXMLObjects().add(cb);

        final ChannelBindings cb2 = XMLObjectProviderRegistrySupport.getBuilderFactory().<ChannelBindings>getBuilderOrThrow(
                ChannelBindings.DEFAULT_ELEMENT_NAME).buildObject(ChannelBindings.DEFAULT_ELEMENT_NAME);
        cb2.setValue("bar");
        ext.getUnknownXMLObjects().add(cb2);

        final ExtractChannelBindingsExtensionsHandler handler = new ExtractChannelBindingsExtensionsHandler();
        handler.initialize();
        
        handler.invoke(messageCtx);
        final ChannelBindingsContext cbCtx = messageCtx.getSubcontext(ChannelBindingsContext.class);
        Assert.assertNotNull(cbCtx);
        Assert.assertEquals(cbCtx.getChannelBindings().size(), 2);
        
        final ChannelBindings[] array = cbCtx.getChannelBindings().toArray(new ChannelBindings[2]);
        Assert.assertTrue("foo".equals(array[0].getValue()) || "bar".equals(array[0].getValue()));
        Assert.assertTrue("foo".equals(array[1].getValue()) || "bar".equals(array[1].getValue()));
    }

}