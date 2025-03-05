//package com.example.auth.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
//import org.springframework.security.oauth2.jwt.JwtEncoder;
//import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2TokenEndpointConfigurer;
//import org.springframework.security.oauth2.server.authorization.token.*;
//
//@Configuration
//public class PasswordGrantTypeConfig {
//
//
//
//    @Bean
//    public OAuth2TokenGenerator<?> tokenGenerator(
//            JwtEncoder jwtEncoder,
//            OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer
//    ) {
//        JwtGenerator jwtGenerator = new JwtGenerator(jwtEncoder);
//        jwtGenerator.setJwtCustomizer(jwtCustomizer);
////        jwtGenerator.setJwsAlgorithm(MacAlgorithm.HS256); // 这里明确指定 HS256
//        return new DelegatingOAuth2TokenGenerator(jwtGenerator, new OAuth2AccessTokenGenerator());
//    }
////
//    @Bean
//    public OAuth2TokenEndpointConfigurer tokenEndpointConfigurer(
//            AuthenticationManager authenticationManager,
//            OAuth2TokenGenerator<?> tokenGenerator
//    ) {
////        return new OAuth2TokenEndpointConfigurer()
////                .authenticationManager(authenticationManager)
////                .tokenGenerator(tokenGenerator)
////                .allowedRequestMatchers(
////                        request -> request.getParameter("grant_type") != null &&
////                                request.getParameter("grant_type").equals("password")
////                );
//        return null;
//    }
//}
