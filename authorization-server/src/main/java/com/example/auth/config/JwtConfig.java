package com.example.auth.config;


import com.example.auth.bean.User;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;


import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.stream.Collectors;

/**
 * JWT 令牌配置
 */
@Configuration
public class JwtConfig {

    // 实际应用中应从安全配置读取密钥
    private static final String SECRET_KEY = "my-secret-key-1234567890-1234567890-1234567890";

    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(getSecretKey()));
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withSecretKey(getSecretKey())
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }

    private SecretKey getSecretKey() {
        return new SecretKeySpec(SECRET_KEY.getBytes(), "HmacSHA256");
    }

    // 自定义令牌内容（添加用户角色）
    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer() {
        return context -> {
            if (context.getTokenType().getValue().equals("access_token")) {
                User user = (User) context.getPrincipal().getPrincipal();
                context.getClaims().claim("roles",
                        user.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList())
                );
            }
        };
    }
}


