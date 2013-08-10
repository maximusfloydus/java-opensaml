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

package org.opensaml.saml.saml2.metadata.provider;

import java.io.File;
import java.net.URL;

import net.shibboleth.utilities.java.support.resolver.CriteriaSet;
import net.shibboleth.utilities.java.support.resolver.ResolverException;

import org.opensaml.core.xml.XMLObjectBaseTestCase;
import org.opensaml.saml.criterion.EntityIdCriterion;
import org.opensaml.saml.saml2.metadata.EntityDescriptor;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ChainingMetadataProviderTest extends XMLObjectBaseTestCase {

    private ChainingMetadataProvider metadataProvider;

    private String entityID;

    private String entityID2;

    /** {@inheritDoc} */
    @BeforeMethod
    protected void setUp() throws Exception {
        entityID = "urn:mace:incommon:washington.edu";
        entityID2 = "urn:mace:switch.ch:SWITCHaai:ethz.ch";

        metadataProvider = new ChainingMetadataProvider();

        URL mdURL = FilesystemMetadataProviderTest.class
                .getResource("/data/org/opensaml/saml/saml2/metadata/InCommon-metadata.xml");
        File mdFile = new File(mdURL.toURI());
        FilesystemMetadataProvider fileProvider = new FilesystemMetadataProvider(mdFile);
        fileProvider.setParserPool(parserPool);
        fileProvider.initialize();
        metadataProvider.addMetadataProvider(fileProvider);

        URL mdURL2 = FilesystemMetadataProviderTest.class
                .getResource("/data/org/opensaml/saml/saml2/metadata/metadata.switchaai_signed.xml");
        File mdFile2 = new File(mdURL2.toURI());
        FilesystemMetadataProvider fileProvider2 = new FilesystemMetadataProvider(mdFile2);
        fileProvider2.setParserPool(parserPool);
        fileProvider2.initialize();
        metadataProvider.addMetadataProvider(fileProvider2);
    }

    //TODO turn on when resolver is refactored
    @Test(enabled=false)
    public void testGetEntityDescriptor() throws ResolverException {
        EntityDescriptor descriptor = metadataProvider.resolveSingle(new CriteriaSet(new EntityIdCriterion(entityID)));
        Assert.assertNotNull(descriptor, "Retrieved entity descriptor was null");
        Assert.assertEquals(descriptor.getEntityID(), entityID, "Entity's ID does not match requested ID");

        EntityDescriptor descriptor2 = metadataProvider.resolveSingle(new CriteriaSet(new EntityIdCriterion(entityID2)));
        Assert.assertNotNull(descriptor2, "Retrieved entity descriptor was null");
        Assert.assertEquals(descriptor2.getEntityID(), entityID2, "Entity's ID does not match requested ID");
    }

    //TODO turn on when resolver is refactored
    @Test(enabled=false)
    public void testFilterDisallowed() {
        try {
            metadataProvider.setMetadataFilter(new SchemaValidationFilter(new String[] {}));
            Assert.fail("Should fail with an UnsupportedOperationException");
        } catch (ResolverException e) {
            Assert.fail("Should fail with an UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected, do nothing
        }
    }

}