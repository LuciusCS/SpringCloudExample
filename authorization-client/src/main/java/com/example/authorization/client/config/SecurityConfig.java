package com.example.authorization.client.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestCustomizers;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, ClientRegistrationRepository registrationRepository) throws Exception {

        String base_uri = OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI;

        DefaultOAuth2AuthorizationRequestResolver resolver = new DefaultOAuth2AuthorizationRequestResolver(registrationRepository, base_uri);

        resolver.setAuthorizationRequestCustomizer(OAuth2AuthorizationRequestCustomizers.withPkce());


        http.authorizeHttpRequests((requests) -> requests.requestMatchers("/").permitAll().anyRequest().authenticated()).
                oauth2Login((login) -> {
                    login.loginPage("/oauth2/authorization/oidc-client");
//                        login.redirectionEndpoint((endpoint) -> endpoint.baseUri("/login/oauth2/callback/*"))
                    login.authorizationEndpoint(authorizationEndpointConfig -> authorizationEndpointConfig.authorizationRequestResolver(resolver));
                })
                .oauth2Client(Customizer.withDefaults());


        return http.build();


    }

}
