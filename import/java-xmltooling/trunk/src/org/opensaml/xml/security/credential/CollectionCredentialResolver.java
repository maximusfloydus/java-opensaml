/*
 * Copyright [2007] [University Corporation for Advanced Internet Development, Inc.]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opensaml.xml.security.credential;

import java.util.ArrayList;
import java.util.Collection;

import org.opensaml.xml.security.CriteriaSet;
import org.opensaml.xml.security.SecurityException;

/**
 * An implementation of {@link CredentialResolver} which uses a {@link Collection} as the
 * underlying credential source.  The credentials returned are filtered based on any
 * {@link EvaluableCredentialCriteria} which may have been present in the specified criteria set.
 */
public class CollectionCredentialResolver extends AbstractCriteriaFilteringCredentialResolver {
    
    /** The collection of credentials which is the underlying store for the resolver. */
    private Collection<Credential> collection;
    
    /**
     * Constructor.
     * 
     * An {@link ArrayList} is used as the underlying collection implementation.
     *
     */
    public CollectionCredentialResolver() {
        super();
        collection = new ArrayList<Credential>();
    }
    
    /**
     * Constructor.
     *
     * @param credentials the credential collection which is the backing store for the resolver
     */
    public CollectionCredentialResolver(Collection<Credential> credentials) {
        super();
        collection = credentials;
    }
    
    /**
     * Get the (modifiable) credential collection which is the backing store for the resolver.
     * 
     * @return the credential collection backing store
     */
    public Collection<Credential> getCollection() {
        return collection;
    }

    /** {@inheritDoc} */
    protected Iterable<Credential> resolveFromSource(CriteriaSet criteriaSet) throws SecurityException {
        return collection;
    }

}
