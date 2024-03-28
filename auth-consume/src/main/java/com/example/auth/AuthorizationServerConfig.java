package com.example.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.context.SecurityContext;
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
