package com.example.auth.controller;


import com.example.auth.bean.OAuth2RegisteredClient;
import com.example.auth.services.OAuthClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RegisteredClientController {

    @Autowired
    OAuthClientService clientService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/addRegisteredClient")
    public boolean addRegisteredClient(@RequestBody OAuth2RegisteredClient client) {
        client.setClientSecret(passwordEncoder.encode(client.getClientSecret()));
        return clientService.save(client);
    }

    ///用于获取所有注册用户的信息
    @GetMapping("/getAllClient")
    public List<OAuth2RegisteredClient> getAllClient() {
        return  clientService.findAll();

    }

}
