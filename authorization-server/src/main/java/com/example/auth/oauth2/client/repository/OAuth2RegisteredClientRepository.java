package com.example.auth.oauth2.client.repository;
import com.example.auth.bean.OAuth2RegisteredClient;
//import org.springframework.security.oauth2.client.registration.ClientRegistration;

import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 动态客户端注册 (RegisteredClientRepository)
 */
@Component
public class OAuth2RegisteredClientRepository implements RegisteredClientRepository {

    private final OAuth2ClientRepository clientRepository;

    public OAuth2RegisteredClientRepository(OAuth2ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        OAuth2RegisteredClient client = clientRepository.findByClientId(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Client not found"));

        // 解析授权类型
        Set<AuthorizationGrantType> grantTypes = Arrays.stream(
                        client.getAuthorizationGrantTypes().split(","))
                .map(String::trim)
                .map(this::resolveGrantType)
                .collect(Collectors.toSet());

        // 解析客户端认证方法
        Set<ClientAuthenticationMethod> authMethods = Arrays.stream(
                        client.getClientAuthenticationMethods().split(","))
                .map(String::trim)
                .map(this::resolveAuthMethod)
                .collect(Collectors.toSet());

        RegisteredClient.Builder builder = RegisteredClient.withId(client.getId().toString())
                .clientId(client.getClientId())
                .clientSecret(client.getClientSecret())
                .clientAuthenticationMethods(methods -> methods.addAll(authMethods))
                .authorizationGrantTypes(types -> types.addAll(grantTypes))
                .scopes(scopes -> scopes.addAll(Arrays.asList(client.getScopes().split(","))));

        // 处理重
        // 定向URI（授权码模式）
        if (grantTypes.contains(AuthorizationGrantType.AUTHORIZATION_CODE)) {
            Arrays.stream(client.getRedirectUris().split(","))
                    .map(String::trim)
                    .forEach(builder::redirectUri);
        }

        return builder.build();
    }
//
//    private AuthorizationGrantType resolveGrantType(String grantType) {
//        return switch (grantType.toLowerCase()) {
//            case "authorization_code" -> AuthorizationGrantType.AUTHORIZATION_CODE;
//            case "password" -> AuthorizationGrantType.PASSWORD;
//            case "client_credentials" -> AuthorizationGrantType.CLIENT_CREDENTIALS;
//            case "refresh_token" -> AuthorizationGrantType.REFRESH_TOKEN;
//            default -> throw new IllegalArgumentException("Unsupported grant type: " + grantType);
//        };
//    }
//
//    private ClientAuthenticationMethod resolveAuthMethod(String method) {
//        return switch (method.toLowerCase()) {
//            case "client_secret_basic" -> ClientAuthenticationMethod.CLIENT_SECRET_BASIC;
//            case "client_secret_post" -> ClientAuthenticationMethod.CLIENT_SECRET_POST;
//            case "none" -> ClientAuthenticationMethod.NONE;
//            default -> throw new IllegalArgumentException("Unsupported method: " + method);
//        };
//    }
private AuthorizationGrantType resolveGrantType(String grantType) {
    if (AuthorizationGrantType.AUTHORIZATION_CODE.getValue().equals(grantType)) {
        return AuthorizationGrantType.AUTHORIZATION_CODE;
    } else if (AuthorizationGrantType.CLIENT_CREDENTIALS.getValue().equals(grantType)) {
        return AuthorizationGrantType.CLIENT_CREDENTIALS;
    } else if (AuthorizationGrantType.REFRESH_TOKEN.getValue().equals(grantType)) {
        return AuthorizationGrantType.REFRESH_TOKEN;
    } else if(AuthorizationGrantType.PASSWORD.getValue().equals(grantType)){
        return AuthorizationGrantType.PASSWORD;

    }else {
        throw new IllegalArgumentException("不支持的授权类型: " + grantType);
    }
}

    // 解析客户端认证方法（例如：client_secret_basic、client_secret_post）
    // 解析客户端认证方法， 老版本的授权类型解析不是这种
    private  ClientAuthenticationMethod resolveAuthMethod(String clientAuthenticationMethod) {
        if (ClientAuthenticationMethod.CLIENT_SECRET_BASIC.getValue().equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.CLIENT_SECRET_BASIC;
        } else if (ClientAuthenticationMethod.CLIENT_SECRET_POST.getValue().equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.CLIENT_SECRET_POST;
        } else if (ClientAuthenticationMethod.CLIENT_SECRET_JWT.getValue().equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.CLIENT_SECRET_JWT;
        } else if (ClientAuthenticationMethod.PRIVATE_KEY_JWT.getValue().equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.PRIVATE_KEY_JWT;
        } else {
            return ClientAuthenticationMethod.NONE.getValue().equals(clientAuthenticationMethod) ? ClientAuthenticationMethod.NONE : new ClientAuthenticationMethod(clientAuthenticationMethod);
        }
    }

    @Override
    public void save(RegisteredClient registeredClient) { /* 动态注册逻辑（可选） */
//            clientRepository.save(registeredClient);
    }

    @Override
    public RegisteredClient findById(String id) {
        return clientRepository.findByClientId(id)
                .map(client -> this.findByClientId(client.getClientId()))
                .orElse(null);
    }
}