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

package org.opensaml.util.xml;

import java.io.InputStream;
import java.io.Reader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.validation.Schema;

import org.w3c.dom.Document;

//TODO needs to implement destructable and verifiable component
//TODO may need to implement initializable component

/** A pool of XML parsers. */
public interface ParserPool {

    /**
     * Gets a builder from the pool.
     * 
     * @return a builder from the pool
     * 
     * @throws XMLParserException thrown if the document builder factory is misconfigured
     */
    public DocumentBuilder getBuilder() throws XMLParserException;

    /**
     * Returns a builder to the pool.
     * 
     * @param builder the builder to return
     */
    public void returnBuilder(DocumentBuilder builder);

    /**
     * Convenience method for creating a new document with a pooled builder.
     * 
     * @return created document
     * 
     * @throws XMLParserException thrown if there is a problem retrieving a builder
     */
    public Document newDocument() throws XMLParserException;

    /**
     * Convenience method for parsing an XML file using a pooled builder.
     * 
     * @param input XML to parse
     * 
     * @return parsed document
     * 
     * @throws XMLParserException thrown if there is a problem retrieving a builder, the input stream can not be read,
     *             or the XML was invalid
     */
    public Document parse(final InputStream input) throws XMLParserException;

    /**
     * Convenience method for parsing an XML file using a pooled builder.
     * 
     * @param input XML to parse
     * 
     * @return parsed document
     * 
     * @throws XMLParserException thrown if there is a problem retrieving a builder, the input stream can not be read,
     *             or the XML was invalid
     */
    public Document parse(final Reader input) throws XMLParserException;

    /**
     * Gets the schema builders use to validate.
     * 
     * @return the schema builders use to validate
     */
    public Schema getSchema();

    /**
     * Sets the schema builders use to validate.
     * 
     * @param newSchema the schema builders use to validate
     */
    public void setSchema(final Schema newSchema);

}