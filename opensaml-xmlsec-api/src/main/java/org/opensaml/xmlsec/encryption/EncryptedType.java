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

package org.opensaml.xmlsec.encryption;

import javax.annotation.Nullable;
import javax.xml.namespace.QName;

import org.opensaml.core.xml.XMLObject;
import org.opensaml.xmlsec.encryption.support.EncryptionConstants;
import org.opensaml.xmlsec.signature.KeyInfo;

/**
 * XMLObject representing XML Encryption, version 20021210, EncryptedType type. This is the base type for
 * {@link EncryptedData} and {@link EncryptedKey} types.
 */
public interface EncryptedType extends XMLObject {

    /** Local name of the XSI type. */
    public static final String TYPE_LOCAL_NAME = "EncryptedType";

    /** QName of the XSI type. */
    public static final QName TYPE_NAME = new QName(EncryptionConstants.XMLENC_NS, TYPE_LOCAL_NAME,
            EncryptionConstants.XMLENC_PREFIX);

    /** Id attribute name. */
    public static final String ID_ATTRIB_NAME = "Id";

    /** Type attribute name. */
    public static final String TYPE_ATTRIB_NAME = "Type";

    /** MimeType attribute name. */
    public static final String MIMETYPE_ATTRIB_NAME = "MimeType";

    /** Encoding attribute name. */
    public static final String ENCODING_ATTRIB_NAME = "Encoding";

    /**
     * Gets the unique ID for the XML element.
     * 
     * @return the unique ID for the XML element
     */
    @Nullable public String getID();

    /**
     * Sets the unique ID for the XML element.
     * 
     * @param newID the unique ID for the XML element
     */
    public void setID(@Nullable final String newID);

    /**
     * Gets the type information for the plaintext content.
     * 
     * @return the type information for the plaintext content
     */
    @Nullable public String getType();

    /**
     * Sets the type information for the plaintext content.
     * 
     * @param newType the type information for the plaintext content
     */
    public void setType(@Nullable final String newType);

    /**
     * Gets the MIME type of the plaintext content.
     * 
     * @return the MIME type of the plaintext content
     */
    @Nullable public String getMimeType();

    /**
     * Sets the MIME type of the plaintext content.
     * 
     * @param newMimeType the MIME type of the plaintext content
     */
    public void setMimeType(@Nullable final String newMimeType);

    /**
     * Gets the encoding applied to the plaintext content prior to encryption.
     * 
     * @return the encoding applied to the plaintext content prior to encryption
     */
    @Nullable public String getEncoding();

    /**
     * Sets the encoding applied to the plaintext content prior to encryption.
     * 
     * @param newEncoding the encoding applied to the plaintext content prior to encryption
     */
    public void setEncoding(@Nullable final String newEncoding);

    /**
     * Gets the EncryptionMethod child element.
     * 
     * @return the EncryptionMethod child element
     */
    @Nullable public EncryptionMethod getEncryptionMethod();

    /**
     * Sets the EncryptionMethod child element.
     * 
     * @param newEncryptionMethod the new EncryptionMethod child element
     */
    public void setEncryptionMethod(@Nullable final EncryptionMethod newEncryptionMethod);

    /**
     * Gets the KeyInfo child element.
     * 
     * @return the KeyInfo child element
     */
    @Nullable public KeyInfo getKeyInfo();

    /**
     * Sets the KeyInfo child element.
     * 
     * @param newKeyInfo the new KeyInfo child element
     */
    public void setKeyInfo(@Nullable final KeyInfo newKeyInfo);

    /**
     * Gets the CipherData child element.
     * 
     * @return the CipherData child element
     */
    @Nullable public CipherData getCipherData();

    /**
     * Sets the CipherData child element.
     * 
     * @param newCipherData the new CipherData child element
     */
    public void setCipherData(@Nullable final CipherData newCipherData);

    /**
     * Gets the EncryptionProperties child element.
     * 
     * @return the EncryptionProperties child element
     */
    @Nullable public EncryptionProperties getEncryptionProperties();

    /**
     * Sets the EncryptionProperties child element.
     * 
     * @param newEncryptionProperties the new EncryptionProperties child element
     */
    public void setEncryptionProperties(@Nullable final EncryptionProperties newEncryptionProperties);

}