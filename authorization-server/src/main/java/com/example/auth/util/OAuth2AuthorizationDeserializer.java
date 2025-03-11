package com.example.auth.util;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;

public class OAuth2AuthorizationDeserializer extends JsonDeserializer<OAuth2Authorization> {


    @Override
    public OAuth2Authorization deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        System.out.println("反序列化前的 JSON 数据: " + node.toPrettyString());
        // 从 JSON 中提取必要字段
        String id = node.get("id").asText();
        String registeredClientId = node.get("registeredClientId").asText();
        String principalName = node.get("principalName").asText();
        String authorizationGrantTypeValue = node.get("authorizationGrantType").get("value").asText();

        // 使用 Builder 创建 OAuth2Authorization 对象
        OAuth2Authorization.Builder builder = OAuth2Authorization.withRegisteredClient(
                        RegisteredClient.withId(registeredClientId).build())
                .id(id)
                .principalName(principalName)
                .authorizationGrantType(new AuthorizationGrantType(authorizationGrantTypeValue));

        // 反序列化 accessToken
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

        // 反序列化 refreshToken
        if (node.has("refreshToken")) {
            JsonNode refreshTokenNode = node.get("refreshToken").get("token");
            OAuth2RefreshToken refreshToken = new OAuth2RefreshToken(
                    refreshTokenNode.get("tokenValue").asText(),
                    Instant.ofEpochMilli((long) (refreshTokenNode.get("issuedAt").asDouble() * 1000)),
                    Instant.ofEpochMilli((long) (refreshTokenNode.get("expiresAt").asDouble() * 1000))
            );
            builder.refreshToken(refreshToken);
        }

        // 反序列化其他字段（如 attributes）
        if (node.has("attributes")) {
            Map<String, Object> attributes = p.getCodec().treeToValue(node.get("attributes"), Map.class);
            builder.attributes(attrs -> attrs.putAll(attributes));
        }

        return builder.build();
    }
}