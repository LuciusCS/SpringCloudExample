package com.example.auth.services;

import com.example.auth.bean.OAuth2RegisteredClient;
import com.example.auth.oauth2.client.repository.OAuth2ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class OAuthClientServiceImpl implements  OAuthClientService{


    @Autowired
    OAuth2ClientRepository clientRepository;

    @Override
    public boolean save(OAuth2RegisteredClient registeredClient) {

        clientRepository.save(registeredClient);
        return true;
    }
}
