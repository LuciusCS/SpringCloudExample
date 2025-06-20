package com.example.authorization.client.config;

import com.example.authorization.client.client.HelloClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.DefaultReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class WebClientConfig {

//    @Bean
//    public HelloClient helloClient(OAuth2AuthorizedClientManager auth2AuthorizedClientManager) throws Exception{
//
//        return httpServiceProxyFactory(auth2AuthorizedClientManager).createClient(HelloClient.class);
//
//    }
//
//    //需要查一下这是做什么的
//    private HttpServiceProxyFactory httpServiceProxyFactory(OAuth2AuthorizedClientManager auth2AuthorizedClientManager){
//
//        ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2Client=
//                new ServletOAuth2AuthorizedClientExchangeFilterFunction(auth2AuthorizedClientManager);
//
//        oauth2Client.setDefaultOAuth2AuthorizedClient(true);
//
//        WebClient webClient= WebClient.builder()
//                .apply(oauth2Client.oauth2Configuration())
//                .build();
//
//        WebClientAdapter client=WebClientAdapter.create(webClient);
//
//        return HttpServiceProxyFactory.builderFor(client).build();
//    }




    // 下面的代码是使用 Spring MVC的模式
//    @Bean
//    public OAuth2AuthorizedClientManager auth2AuthorizedClientManager(
//            ClientRegistrationRepository clientRegistrationRepository,
//            OAuth2AuthorizedClientRepository auth2AuthorizedClientRepository
//    ){
//        OAuth2AuthorizedClientProvider auth2AuthorizedClientProvider=
//                OAuth2AuthorizedClientProviderBuilder.builder()
//                        .authorizationCode()
//                        .refreshToken()
//                        .build();
//
//        DefaultOAuth2AuthorizedClientManager auth2AuthorizedClientManager=
//                new DefaultOAuth2AuthorizedClientManager(clientRegistrationRepository,auth2AuthorizedClientRepository);
//
//        auth2AuthorizedClientManager.setAuthorizedClientProvider(auth2AuthorizedClientProvider);
//
//        return  auth2AuthorizedClientManager;
//
//    }

    /**
     * 下面的代码是使用 webflux模式
     */
//    @Bean
//    public ReactiveOAuth2AuthorizedClientManager reactiveOAuth2AuthorizedClientManager(
//            ReactiveClientRegistrationRepository clientRegistrationRepository,
//            ServerOAuth2AuthorizedClientRepository authorizedClientRepository) {
//
//        ReactiveOAuth2AuthorizedClientProvider authorizedClientProvider =
//                ReactiveOAuth2AuthorizedClientProviderBuilder.builder()
//                        .authorizationCode()
//                        .refreshToken()
//                        .clientCredentials()
//                        .password()
//                        .build();
//
//        DefaultReactiveOAuth2AuthorizedClientManager authorizedClientManager =
//                new DefaultReactiveOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientRepository);
//        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
//
//        return authorizedClientManager;
//    }

}
