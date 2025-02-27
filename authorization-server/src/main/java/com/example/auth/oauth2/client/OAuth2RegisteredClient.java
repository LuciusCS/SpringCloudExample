package com.example.auth.oauth2.client;


import jakarta.persistence.*;
import lombok.Data;

/**
 *
 */

@Entity
@Data
@Table(name = "oauth2_client")
public class OAuth2RegisteredClient
{
    @Id
    private String id;

    @Column(name = "client_id", unique = true, nullable = false)
    private String clientId;

    @Column(name = "client_secret", nullable = false)
    private String clientSecret;

    @Column(name = "authorization_grant_types", nullable = false)
    private String authorizationGrantTypes;  // 如 "client_credentials,authorization_code"

    @Column(name = "scopes", nullable = false)
    private String scopes;

    @Column(name = "redirect_uris")
    private String redirectUris;

    @Column(name = "client_authentication_methods", nullable = false)
    private String clientAuthenticationMethods; // 如 "client_secret_basic"
}
