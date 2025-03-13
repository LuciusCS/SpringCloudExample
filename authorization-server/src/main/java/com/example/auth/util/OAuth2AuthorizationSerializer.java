package com.example.auth.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;

import java.io.IOException;

public class OAuth2AuthorizationSerializer  extends JsonSerializer<OAuth2Authorization> {

    @Override
    public void serialize(OAuth2Authorization authorization, JsonGenerator gen, SerializerProvider serializers) throws IOException {

        gen.writeStartObject();
        gen.writeStringField("id", authorization.getId());
        gen.writeStringField("registeredClientId", authorization.getRegisteredClientId());
        gen.writeStringField("principalName", authorization.getPrincipalName());
        gen.writeObjectField("authorizationGrantType", authorization.getAuthorizationGrantType());
        gen.writeObjectField("authorizedScopes", authorization.getAuthorizedScopes());
        gen.writeObjectField("attributes", authorization.getAttributes());

        // Serialize access token
        if (authorization.getAccessToken() != null) {
            gen.writeObjectField("accessToken", authorization.getAccessToken());
        }

        // Serialize refresh token
        if (authorization.getRefreshToken() != null) {
            gen.writeObjectField("refreshToken", authorization.getRefreshToken());
        }

        gen.writeEndObject();
    }

}