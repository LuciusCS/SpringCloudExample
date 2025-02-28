package com.example.auth.services;

import com.example.auth.bean.OAuth2RegisteredClient;

public interface OAuthClientService {

    public  boolean save(OAuth2RegisteredClient registeredClient);
}
