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

package org.opensaml.saml.metadata.resolver.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Timer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.shibboleth.utilities.java.support.component.ComponentSupport;
import net.shibboleth.utilities.java.support.logic.Constraint;
import net.shibboleth.utilities.java.support.resolver.ResolverException;

import org.joda.time.DateTime;
import org.joda.time.chrono.ISOChronology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A metadata provider that pulls metadata from a file on the local filesystem.
 * 
 * This metadata provider periodically checks to see if the read metadata file has changed. The delay between each
 * refresh interval is calculated as follows. If no validUntil or cacheDuration is present then the
 * {@link #getMaxRefreshDelay()} value is used. Otherwise, the earliest refresh interval of the metadata file is checked
 * by looking for the earliest of all the validUntil attributes and cacheDuration attributes. If that refresh interval
 * is larger than the max refresh delay then {@link #getMaxRefreshDelay()} is used. If that number is smaller than the
 * min refresh delay then {@link #getMinRefreshDelay()} is used. Otherwise the calculated refresh delay multiplied by
 * {@link #getRefreshDelayFactor()} is used. By using this factor, the provider will attempt to be refresh before the
 * cache actually expires, allowing a some room for error and recovery. Assuming the factor is not exceedingly close to
 * 1.0 and a min refresh delay that is not overly large, this refresh will likely occur a few times before the cache
 * expires.
 * 
 */
public class FilesystemMetadataResolver extends AbstractReloadingMetadataResolver {

    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(FilesystemMetadataResolver.class);

    /** The metadata file. */
    @Nonnull private File metadataFile;

    /**
     * Constructor.
     * 
     * @param metadata the metadata file
     * 
     * @throws ResolverException  this exception is no longer thrown
     */
    public FilesystemMetadataResolver(@Nonnull final File metadata) throws ResolverException {
        super();
        setMetadataFile(metadata);
    }

    /**
     * Constructor.
     * 
     * @param metadata the metadata file
     * @param backgroundTaskTimer timer used to refresh metadata in the background
     * 
     * @throws ResolverException  this exception is no longer thrown
     */
    public FilesystemMetadataResolver(@Nullable final Timer backgroundTaskTimer, @Nonnull final File metadata)
            throws ResolverException {
        super(backgroundTaskTimer);
        setMetadataFile(metadata);
    }

    /**
     * Sets the file from which metadata is read.
     * 
     * @param file path to the metadata file
     * 
     * @throws ResolverException this exception is no longer thrown
     */
    protected void setMetadataFile(@Nonnull final File file) throws ResolverException {
        ComponentSupport.ifInitializedThrowUnmodifiabledComponentException(this);
        ComponentSupport.ifDestroyedThrowDestroyedComponentException(this);

        metadataFile = Constraint.isNotNull(file, "Metadata file cannot be null");
    }

    /** {@inheritDoc} */
    @Override
    protected void doDestroy() {
        metadataFile = null;
          
        super.doDestroy();
    }
    
    /** {@inheritDoc} */
    @Override
    protected String getMetadataIdentifier() {
        return metadataFile.getAbsolutePath();
    }

    /** {@inheritDoc} */
    @Override
    protected byte[] fetchMetadata() throws ResolverException {
        try {
            validateMetadataFile(metadataFile);
            DateTime metadataUpdateTime = new DateTime(metadataFile.lastModified(), ISOChronology.getInstanceUTC());
            if (getLastRefresh() == null || getLastUpdate() == null || metadataUpdateTime.isAfter(getLastRefresh())) {
                return inputstreamToByteArray(new FileInputStream(metadataFile));
            }

            return null;
        } catch (IOException e) {
            String errMsg = "Unable to read metadata file " + metadataFile.getAbsolutePath();
            log.error(errMsg, e);
            throw new ResolverException(errMsg, e);
        }
    }
    
    /**
     * Validate the basic properties of the specified metadata file, for example that it exists; 
     * that it is a file; and that it is readable.
     *
     * @param file the file to evaluate
     * @throws ResolverException if file does not pass basic properties required of a metadata file
     */
    protected void validateMetadataFile(@Nonnull final File file) throws ResolverException {
        if (!file.exists()) {
            throw new ResolverException("Metadata file '" + file.getAbsolutePath() + "' does not exist");
        }

        if (!file.isFile()) {
            throw new ResolverException("Metadata file '" + file.getAbsolutePath() + "' is not a file");
        }

        if (!file.canRead()) {
            throw new ResolverException("Metadata file '" + file.getAbsolutePath() + "' is not readable");
        }
    }

}