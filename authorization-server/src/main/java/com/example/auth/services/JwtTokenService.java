package com.example.auth.services;

import com.example.auth.bean.User;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * JWT Token 生成服务
 */
@Service
@Slf4j
public class JwtTokenService {

    private final JwtEncoder jwtEncoder;

    @Autowired
    public JwtTokenService(JWKSource<SecurityContext> jwkSource) {
        this.jwtEncoder = new NimbusJwtEncoder(jwkSource);
    }

    /**
     * 为用户生成JWT Token
     *
     * @param user 用户实体
     * @return JWT Token字符串
     */
    public String generateToken(User user) {
        Instant now = Instant.now();
        Instant expiry = now.plus(7, ChronoUnit.DAYS); // Token有效期7天

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("authorization-server")
                .issuedAt(now)
                .expiresAt(expiry)
                .subject(user.getUsername())
                .claim("userId", user.getId())
                .claim("openid", user.getWechatOpenid())
                .build();

        JwsHeader header = JwsHeader.with(SignatureAlgorithm.RS256).build();

        String token = jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
        log.info("为用户生成JWT Token{}, userId: {}, username: {}",token, user.getId(), user.getUsername());

        return token;
    }
}
