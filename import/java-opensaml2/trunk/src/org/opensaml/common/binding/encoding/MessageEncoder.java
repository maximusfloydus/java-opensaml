/*
 * Copyright [2006] [University Corporation for Advanced Internet Development, Inc.]
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

package org.opensaml.common.binding.encoding;

import javax.servlet.ServletResponse;

import org.opensaml.common.SAMLObject;
import org.opensaml.common.binding.BindingException;
import org.opensaml.saml2.metadata.Endpoint;
import org.opensaml.saml2.metadata.EntityDescriptor;
import org.opensaml.saml2.metadata.RoleDescriptor;
import org.opensaml.saml2.metadata.provider.MetadataProvider;
import org.opensaml.xml.security.credential.Credential;

/**
 * Encodes a SAML message in a binding specific manner. A given encoder instance need not be thread-safe or reusable.
 * The process of encoding a message may change some of the properties set on this encoder. For example, a message may
 * be required to be signed in a specific manner, so prior to the encoding the message may not be signed while
 * afterwords it may be.
 * 
 * @param <ResponseType> the type of response the message is encoded to
 */
public interface MessageEncoder<ResponseType extends ServletResponse> {
    
    /**
     * Encode the SAML message in the binding specific manner.
     * 
     * @throws BindingException thrown if the problem can not be encoded
     */
    public void encode() throws BindingException;

    /**
     * Gets the binding URI supported by this encoder.
     * 
     * @return binding URI supported by this encoder
     */
    public String getBindingURI();

    /**
     * Gets the issuer of the message.
     * 
     * @return issuer of the message
     */
    public String getIssuer();
    
    /**
     * Gets the metadata provider that can be used to look up information about the relying party.
     * 
     * @return the metadata provider that can be used to look up information about the relying party
     */
    public MetadataProvider getMetadataProvider();
    
    /**
     * Gets the relay state associated with the outgoing message.
     * 
     * @return relay state associated with the outgoing message
     */
    public String getRelayState();
    
    /**
     * Sets the relay state associated with the outgoing message.
     * 
     * @param relayState relay state associated with the outgoing message
     */
    public void setRelayState(String relayState);
    
    /**
     * Gets the relying party the message will be encoded for.
     * 
     * @return relying party the message will be encoded for
     */
    public EntityDescriptor getRelyingParty();

    /**
     * Gets the endpoint to which the message will be sent.
     * 
     * @return endpoint to which the message will be sent
     */
    public Endpoint getRelyingPartyEndpoint();

    /**
     * Gets the role of the relying party the message will be encoded for.
     * 
     * @return role of the relying party the message will be encoded for
     */
    public RoleDescriptor getRelyingPartyRole();

    /**
     * Sets the response to use during the encoding process.
     * 
     * @return response the response to use during encoding
     */
    public ResponseType getResponse();

    /**
     * Gets the SAML message that will be encoded and sent to the relying party.
     * 
     * @return the SAML message that will be encoded and sent to the relying party
     */
    public SAMLObject getSamlMessage();

    /**
     * Gets the credential that should be used to sign the message.
     * 
     * @return credential that should be used to sign the message
     */
    public Credential getSigningCredential();

    /**
     * Sets the issuer of the message.
     * 
     * @param issuer issuer of the message
     */
    public void setIssuer(String issuer);

    /**
     * Sets the metadata provider that can be used to look up information about the relying party.
     * 
     * @param metadatProvider provider that can be used to look up information about the relying party, may not be null
     */
    public void setMetadataProvider(MetadataProvider metadatProvider);

    /**
     * Sets relying party the message will be encoded for.
     * 
     * @param relyingParty relying party the message will be encoded for, may not be null
     */
    public void setRelyingParty(EntityDescriptor relyingParty);
    
    /**
     * Sets the endpoint to which the message will be sent.
     * 
     * @param endpoint endpoint to which the message will be sent
     */
    public void setRelyingPartyEndpoint(Endpoint endpoint);
    
    /**
     * Sets the role of the relying party the message will be encoded for.
     * 
     * @param relyingPartyRole role of the relying party the message will be encoded for
     */
    public void setRelyingPartyRole(RoleDescriptor relyingPartyRole);

    /**
     * Sets the response to use during the encoding process.
     * 
     * @param response the response to use during encoding
     */
    public void setResponse(ResponseType response);

    /**
     * Sets the SAML message that will be encoded and sent to the relying party.
     * 
     * @param samlMessage the SAML message to encode, may not be null
     */
    public void setSamlMessage(SAMLObject samlMessage);

    /**
     * Sets the credential that should be used to sign the message.
     * 
     * @param credential credential that should be used to sign the message
     */
    public void setSigningCredential(Credential credential);
}