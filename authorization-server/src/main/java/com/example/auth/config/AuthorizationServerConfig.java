package com.example.auth.config;

import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//@Configuration
//@Import(OAuth2AuthorizationServerConfiguration.class)
public class AuthorizationServerConfig {

    ///在WebSecurityConfig中有
//    @Bean
//    public RegisteredClientRepository registeredClientRepository(){
//        List<RegisteredClient>registeredClients= new ArrayList<>();
//        return  new InMemoryRegisteredClientRepository(registeredClients);
//
//
//    }

    ///在WebSecurityConfig中有
//    @Bean
//    public JWKSource<SecurityContext>jwkSource(){
//        RSAKey rsaKey = new RSAKey.Builder(publicKey)
//                .privateKey(privateKey)
//                .keyID(UUID.randomUUID().toString())
//                .build();
//    }

}
