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
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.io.IOException;
import java.security.Principal;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class OAuth2AuthorizationDeserializer extends JsonDeserializer<OAuth2Authorization> {
    private final RegisteredClientRepository clientRepository;

    public OAuth2AuthorizationDeserializer(RegisteredClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public OAuth2Authorization deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException {

        JsonNode node = p.getCodec().readTree(p);

        // 基础字段解析
        String id = node.get("id").asText();
        String registeredClientId = node.get("registeredClientId").asText();
        String principalName = node.get("principalName").asText();
        AuthorizationGrantType grantType = new AuthorizationGrantType(
                node.get("authorizationGrantType").get("value").asText()
        );

        // 动态查询 RegisteredClient
//        RegisteredClient registeredClient = clientRepository.findById(registeredClientId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid client id"));
//        RegisteredClient registeredClient = Objects.requireNonNull(clientRepository.findById("5"),
//                "Invalid client id");
        RegisteredClient registeredClient =   RegisteredClient.withId("5")
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
                .build();

        OAuth2Authorization.Builder builder = OAuth2Authorization.withRegisteredClient(registeredClient)
                .id(id)
                .principalName(principalName)
                .authorizationGrantType(grantType);

        // 解析授权范围
        if (node.has("authorizedScopes")) {
            Set<String> scopes = new LinkedHashSet<>();
            node.get("authorizedScopes").elements()
                    .forEachRemaining(e -> scopes.add(e.asText()));
            builder.authorizedScopes(scopes);
        }

        // 解析访问令牌
        if (node.has("accessToken")) {
            JsonNode tokenNode = node.get("accessToken").get("token");
            OAuth2AccessToken accessToken = parseAccessToken(tokenNode);
            builder.accessToken(accessToken);
        }

        // 解析刷新令牌
        if (node.has("refreshToken")) {
            JsonNode tokenNode = node.get("refreshToken").get("token");
            OAuth2RefreshToken refreshToken = parseRefreshToken(tokenNode);
            builder.refreshToken(refreshToken);
        }

        // 解析ID Token
        if (node.has("oidcIdToken")) {
            JsonNode tokenNode = node.get("oidcIdToken").get("token");
            OidcIdToken idToken = parseIdToken(tokenNode ,p);
            builder.token(idToken);
        }

        // 解析属性（包含Principal）
        if (node.has("attributes")) {
            Map<String, Object> attributes = p.getCodec()
                    .treeToValue(node.get("attributes"), Map.class);
            builder.attributes(attrs -> attrs.putAll(attributes));
        }

        return builder.build();
    }

    private OAuth2AccessToken parseAccessToken(JsonNode tokenNode) {
        return new OAuth2AccessToken(
                OAuth2AccessToken.TokenType.BEARER,
                tokenNode.get("tokenValue").asText(),
                parseInstant(tokenNode.get("issuedAt")),
                parseInstant(tokenNode.get("expiresAt"))
        );
    }

    private OidcIdToken parseIdToken(JsonNode tokenNode, JsonParser p) throws IOException {
        return new OidcIdToken(
                tokenNode.get("tokenValue").asText(),
                parseInstant(tokenNode.get("issuedAt")),
                parseInstant(tokenNode.get("expiresAt")),
                tokenNode.get("claims").traverse(p.getCodec()).readValueAs(Map.class)
        );
    }

    private Instant parseInstant(JsonNode node) {
        return Instant.ofEpochMilli((long) (node.asDouble() * 1000));
    }


    private OAuth2RefreshToken parseRefreshToken(JsonNode tokenNode) {
        String tokenValue = tokenNode.get("tokenValue").asText();
        Instant issuedAt = parseInstant(tokenNode.get("issuedAt"));
        Instant expiresAt = parseInstant(tokenNode.get("expiresAt"));

        return new OAuth2RefreshToken(tokenValue, issuedAt, expiresAt);
    //        {
    //            @Override
    //            public String getTokenValue(    ) {
    //                return tokenValue;
    //            }
    //
    //            @Override
    //            public Instant getIssuedAt() {
    //                return issuedAt;
    //            }
    //
    //            @Override
    //            public Instant getExpiresAt() {
    //                return expiresAt;
    //            }
    //        };
    }
}