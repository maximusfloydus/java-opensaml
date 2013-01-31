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

package org.opensaml.security.credential.criteria.impl;

import org.opensaml.security.credential.BasicCredential;
import org.opensaml.security.criteria.EntityIDCriterion;
import org.opensaml.security.crypto.KeySupport;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 */
public class EvaluableEntityIDCredentialCriterionTest {
    
    private BasicCredential credential;
    private String entityID;
    private EntityIDCriterion criteria;
    
    public EvaluableEntityIDCredentialCriterionTest() {
        entityID = "someEntityID";
    }

    @BeforeMethod
    protected void setUp() throws Exception {
        credential = new BasicCredential(KeySupport.generateKey("AES", 128, null));
        credential.setEntityId(entityID);
        
        criteria = new EntityIDCriterion(entityID);
    }
    
    @Test
    public void testSatisfy() {
        EvaluableEntityIDCredentialCriterion evalCrit = new EvaluableEntityIDCredentialCriterion(criteria);
        Assert.assertTrue(evalCrit.evaluate(credential), "Credential should have matched the evaluable criteria");
    }

    @Test
    public void testNotSatisfy() {
        criteria.setEntityID("OTHER");
        EvaluableEntityIDCredentialCriterion evalCrit = new EvaluableEntityIDCredentialCriterion(criteria);
        Assert.assertFalse(evalCrit.evaluate(credential), "Credential should NOT have matched the evaluable criteria");
    }
    
    @Test
    public void testCanNotEvaluate() {
        credential.setEntityId(null);
        EvaluableEntityIDCredentialCriterion evalCrit = new EvaluableEntityIDCredentialCriterion(criteria);
        Assert.assertNull(evalCrit.evaluate(credential), "Credential should have been unevaluable against the criteria");
    }
    
    @Test
    public void testRegistry() throws Exception {
        EvaluableCredentialCriterion evalCrit = EvaluableCredentialCriteriaRegistry.getEvaluator(criteria);
        Assert.assertNotNull(evalCrit, "Evaluable criteria was unavailable from the registry");
        Assert.assertTrue(evalCrit.evaluate(credential), "Credential should have matched the evaluable criteria");
    }
}
