package com.example.auth.bean;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

/**
 * 用于表示已经注册的客户端信息
 */

@Entity
@Data
@Table(name = "oauth2_client")
public class OAuth2RegisteredClient
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "客户端 ID，OAuth2 客户端的唯一标识")
    @Column(name = "client_id", unique = true, nullable = false)
    private String clientId;

    @Schema(description = "客户端密")
    @Column(name = "client_secret", nullable = false)
    private String clientSecret;

    @Schema(description = "授权类型")
    @Column(name = "authorization_grant_types", nullable = false)
    private String authorizationGrantTypes;  // 如 "client_credentials,authorization_code"

    @Schema(description = "授权范围")
    @Column(name = "scopes", nullable = false)
    private String scopes;

    @Schema(description = "重定向URI")
    @Column(name = "redirect_uris")
    private String redirectUris;

    /**
     * client_secret_basic	使用 HTTP Basic Auth，将 client_id 和 client_secret 编码后放在 Authorization Header 中。	RFC 6749 §2.3.1
     * client_secret_post	将 client_id 和 client_secret 放在 POST 请求体中（application/x-www-form-urlencoded）。	[RFC 6749 §2.3.1]
     * client_secret_jwt	使用 client_id 和对称密钥（client_secret）签名 JWT 作为认证方式。	[RFC 7523 §2.2]
     * private_key_jwt	 使用客户端私钥签名 JWT 作为认证方式。密钥由授权服务器预先注册。	[RFC 7523 §2.2]
     * none	   客户端不进行身份验证，适用于公开客户端（public client），如前端 JavaScript 应用。
     *
     * 推荐仅使用前两种client_secret_basic 和 client_secret_post
     *
     */
    @Schema(description = "客户端认证方式字段")
    @Column(name = "client_authentication_methods", nullable = false)
    private String clientAuthenticationMethods; // 如 "client_secret_basic"
}
