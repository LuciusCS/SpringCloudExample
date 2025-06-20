package com.example.authorization.client.controller;


import com.example.authorization.client.client.HelloClient;
import com.example.authorization.client.client.HelloFeignClient;
import com.example.authorization.client.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/appController")
public class AppController {

    @Autowired
    private AppService appService;

//    private final HelloClient helloClient;
//    @Autowired
//    private OAuth2AuthorizedClientManager authorizedClientManager;

//    @Autowired
//    private ClientRegistrationRepository clientRegistrationRepository;
//    public AppController(HelloClient helloClient) {
//        this.helloClient = helloClient;
//    }

    @Autowired
    HelloFeignClient helloFeignClient;

    @GetMapping("/")
    public ResponseEntity<String>getPublicData(){
        return  ResponseEntity.ok("public data");
    }

//    @GetMapping("/private-data")
//    public ResponseEntity<String>getPrivateData(){
//        return ResponseEntity.ok(appService.getJwtToken());
//    }

    @GetMapping("/hello")
    public ResponseEntity<String>sayHello(){


        return ResponseEntity.ok(helloFeignClient.getHello());
//        return helloFeignClient.;
    }

//    @GetMapping("/token")
//    public String getToken() {
//        // client_credentials 模式下，Principal 可以是任意非 null 值
//        Authentication principal = new AnonymousAuthenticationToken(
//                "key", "anonymousUser", AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
//
//        OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
//                .withClientRegistrationId("client1234") // 你的 client id 名字
//                .principal(principal)
//                .build();
//
////        OAuth2AuthorizedClient authorizedClient =
////                authorizedClientManager.authorize(authorizeRequest);
//
//        if (authorizedClient == null) {
//            throw new RuntimeException("授权失败，可能配置不正确");
//        }
//
//        return authorizedClient.getAccessToken().getTokenValue();
//    }



}
