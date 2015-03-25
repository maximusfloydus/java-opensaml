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

package org.opensaml.soap.soap12;

import javax.annotation.Nullable;
import javax.xml.namespace.QName;

import org.opensaml.soap.util.SOAPConstants;

/**
 * Interface for element having a <code>@soap12:encodingStyle</code> attribute.
 */
public interface EncodingStyleBearing {
    
    /** The soap12:@encodingStyle attribute local name. */
    public static final String SOAP12_ENCODING_STYLE_ATTR_LOCAL_NAME = "encodingStyle";

    /** The soap12:@encodingStyle qualified attribute name. */
    public static final QName SOAP12_ENCODING_STYLE_ATTR_NAME =
        new QName(SOAPConstants.SOAP12_NS, SOAP12_ENCODING_STYLE_ATTR_LOCAL_NAME, SOAPConstants.SOAP12_PREFIX);
    
    /**
     * Get the attribute value.
     * 
     * @return return the attribute value
     */
    @Nullable public String getSOAP12EncodingStyle();
    
    /**
     * Set the attribute value.
     * 
     * @param newEncodingStyle the new attribute value
     */
    public void setSOAP12EncodingStyle(@Nullable final String newEncodingStyle);

}