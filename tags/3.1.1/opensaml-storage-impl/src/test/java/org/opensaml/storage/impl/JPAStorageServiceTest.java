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

package org.opensaml.storage.impl;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.persistence.EntityManagerFactory;

import net.shibboleth.ext.spring.util.SpringSupport;
import net.shibboleth.utilities.java.support.component.ComponentInitializationException;

import org.opensaml.storage.StorageRecord;
import org.opensaml.storage.StorageService;
import org.opensaml.storage.StorageServiceTest;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Test of {@link JPAStorageService} implementation.
 */
public class JPAStorageServiceTest extends StorageServiceTest {

    /** Storage service. */
    private JPAStorageService storageService;

    /** Contexts used for testing. */
    private Object[][] contexts;

    public JPAStorageServiceTest() {
        final SecureRandom random = new SecureRandom();
        contexts = new Object[10][1];
        for (int i = 0; i < 10; i++) {
            contexts[i] = new Object[] {Long.toString(random.nextLong()), };
        }
    }

    /**
     * Creates the shared instance of the entity manager factory.
     */
    @BeforeClass public void setUp() throws ComponentInitializationException {
        storageService = new JPAStorageService(createEntityManagerFactory());
        storageService.setId("test");
        storageService.setCleanupInterval(5000);
        storageService.setTransactionRetry(2);
        super.setUp();
    }

    /**
     * Creates an entity manager factory instance.
     */
    private EntityManagerFactory createEntityManagerFactory() throws ComponentInitializationException
    {
        final Resource resource = new ClassPathResource("/org/opensaml/storage/impl/jpa-spring-context.xml");
        final GenericApplicationContext context = SpringSupport.newContext("JPAStorageService",
                Collections.singletonList(resource), Collections.<BeanFactoryPostProcessor>emptyList(),
                Collections.<BeanPostProcessor>emptyList(), Collections.<ApplicationContextInitializer>emptyList(),
                null);
        FactoryBean<EntityManagerFactory> factoryBean = context.getBean(org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean.class);
        try {
            return factoryBean.getObject();
        } catch (Exception e) {
            throw new ComponentInitializationException(e);
        }
    }

    @AfterClass
    protected void tearDown() {
        try {
            List<String> contexts = storageService.readContexts();
            for (String ctx : contexts) {
                storageService.deleteContext(ctx);
            }
            List<StorageRecord> recs = storageService.readAll();
            Assert.assertEquals(recs.size(), 0);
        } catch (IOException e){ 
            throw new RuntimeException(e);
        }
        super.tearDown();
    }

    @Nonnull protected StorageService getStorageService() {
        return storageService;
    }

    @Test
    public void cleanup() throws ComponentInitializationException, IOException {
        String context = Long.toString(random.nextLong());
        for (int i = 1; i <= 100; i++) {
            storageService.create(context, Integer.toString(i), Integer.toString(i + 1), System.currentTimeMillis() + 100);
        }
        try {
            Thread.sleep(7500);
        } catch (InterruptedException e) {
            throw new IOException(e);
        }
        List<StorageRecord> recs = storageService.readAll(context);
        Assert.assertEquals(recs.size(), 0);
    }

    @DataProvider(name = "contexts")
    public Object[][] contexts() throws Exception {
        return contexts;
    }

    @Test(dataProvider = "contexts", singleThreaded = false, threadPoolSize = 25, invocationCount = 100)
    public void multithread(final String context) throws IOException {
        shared.create(context, "mt", "bar", System.currentTimeMillis() + 300000);
        StorageRecord rec = shared.read(context, "mt");
        Assert.assertNotNull(rec);
        shared.update(context, "mt", "baz", System.currentTimeMillis() + 300000);
        rec = shared.read(context, "mt");
        Assert.assertNotNull(rec);
        boolean result = shared.create(context, "mt", "qux", null);
        Assert.assertFalse(result, "createString should have failed");
    }
}