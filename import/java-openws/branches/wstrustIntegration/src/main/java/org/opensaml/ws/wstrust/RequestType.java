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
package org.opensaml.ws.wstrust;

import javax.xml.namespace.QName;

import org.opensaml.xml.schema.XSURI;

/**
 * The wst:RequestType element.
 * 
 */
public interface RequestType extends XSURI, WSTrustObject {

    /** Element local name. */
    public static final String ELEMENT_LOCAL_NAME = "RequestType";

    /** Default element name */
    public final static QName ELEMENT_NAME =
        new QName(WSTrustConstants.WST_NS, ELEMENT_LOCAL_NAME, WSTrustConstants.WST_PREFIX);
    
    /** Local name of the XSI type. */
    public static final String TYPE_LOCAL_NAME = "RequestTypeOpenEnum"; 
        
    /** QName of the XSI type. */
    public static final QName TYPE_NAME = 
        new QName(WSTrustConstants.WST_NS, TYPE_LOCAL_NAME, WSTrustConstants.WST_PREFIX);

    /** RequestType Issue URI */
    public static final String ISSUE = WSTrustConstants.WST_NS + WSTrustConstants.REQUESTTYPE_ISSUE;

    /** RequestType Renew URI */
    public static final String RENEW = WSTrustConstants.WST_NS + WSTrustConstants.REQUESTTYPE_RENEW;

    /** RequestType Cancel URI */
    public static final String CANCEL = WSTrustConstants.WST_NS + WSTrustConstants.REQUESTTYPE_CANCEL;

    /** RequestType STSCancel URI */
    public static final String STSCANCEL = WSTrustConstants.WST_NS + WSTrustConstants.REQUESTTYPE_STSCANCEL;

    /** RequestType Validate URI */
    public static final String VALIDATE = WSTrustConstants.WST_NS + WSTrustConstants.REQUESTTYPE_VALIDATE;

}
