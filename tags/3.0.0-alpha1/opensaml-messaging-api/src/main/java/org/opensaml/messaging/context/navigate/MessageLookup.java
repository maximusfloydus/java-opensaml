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

package org.opensaml.messaging.context.navigate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.shibboleth.utilities.java.support.logic.Constraint;

import org.opensaml.messaging.context.MessageContext;

/**
 * A {@link Function} that returns the message from a {@link MessageContext}.
 * 
 * @param <T> type of message
 */
public class MessageLookup<T> implements ContextDataLookupFunction<MessageContext, T> {

    /** Child context type to look up. */
    private final Class<T> messageType;
    
    /**
     * Constructor.
     * 
     * @param type message type to look up
     */
    public MessageLookup(@Nonnull final Class<T> type) {
        messageType = Constraint.isNotNull(type, "Message type cannot be null");
    }
    
    /** {@inheritDoc} */
    @Override
    @Nullable public T apply(@Nullable final MessageContext input) {
        if (input != null && messageType.isInstance(input.getMessage())) {
            return (T) input.getMessage();
        }
        return null;
    }

}