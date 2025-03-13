package com.example.auth.util;
import com.example.auth.bean.User;
import com.example.auth.config.extension.password.PasswordAuthenticationToken;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.io.IOException;
import java.security.Principal;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class OAuth2AuthorizationDeserializer extends JsonDeserializer<OAuth2Authorization> {
    @Override
    public OAuth2Authorization deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        String id = node.get("id").asText();
        String registeredClientId = node.get("registeredClientId").asText();
        String principalName = node.get("principalName").asText();
        String authorizationGrantTypeValue = node.get("authorizationGrantType").get("value").asText();

        OAuth2Authorization.Builder builder = OAuth2Authorization.withRegisteredClient(
//                        RegisteredClient.withId(registeredClientId)
                        RegisteredClient.withId("5")
                                .clientId("client1234")
                                .clientSecret("secret")
                                .authorizationGrantType(AuthorizationGrantType.PASSWORD)
                                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                                .redirectUri("http://localhost:8004/callback")
                                .scope("read")
                                .scope("openid")
                                .scope("profile")
                                .scope("write")
                                .clientSettings(ClientSettings.builder()
                                        .requireAuthorizationConsent(false)
                                        .build())
                                .tokenSettings(TokenSettings.builder()
                                        .accessTokenTimeToLive(Duration.ofHours(1))
                                        .build())
                                .build())
                .id(id)
                .principalName(principalName)
                .authorizationGrantType(new AuthorizationGrantType(authorizationGrantTypeValue));

        // Deserialize access token
        if (node.has("accessToken")) {
            JsonNode accessTokenNode = node.get("accessToken").get("token");
            OAuth2AccessToken accessToken = new OAuth2AccessToken(
                    OAuth2AccessToken.TokenType.BEARER,
                    accessTokenNode.get("tokenValue").asText(),
                    Instant.ofEpochMilli((long) (accessTokenNode.get("issuedAt").asDouble() * 1000)),
                    Instant.ofEpochMilli((long) (accessTokenNode.get("expiresAt").asDouble() * 1000))
            );
            builder.accessToken(accessToken);
        }

        // Deserialize refresh token
        if (node.has("refreshToken")) {
            JsonNode refreshTokenNode = node.get("refreshToken").get("token");
            OAuth2RefreshToken refreshToken = new OAuth2RefreshToken(
                    refreshTokenNode.get("tokenValue").asText(),
                    Instant.ofEpochMilli((long) (refreshTokenNode.get("issuedAt").asDouble() * 1000)),
                    Instant.ofEpochMilli((long) (refreshTokenNode.get("expiresAt").asDouble() * 1000))
            );
            builder.refreshToken(refreshToken);
        }

        // Deserialize attributes
//        if (node.has("attributes")) {
//            Map<String, Object> attributes = p.getCodec().treeToValue(node.get("attributes"), Map.class);
//            builder.attributes(attrs -> attrs.putAll(attributes));
//        }

//        builder.
//        builder.
//        builder.principalName("username");

        return builder.build();

    }
}