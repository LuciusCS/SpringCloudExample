package com.example.auth.services;

import com.example.auth.bean.OAuth2RegisteredClient;

import java.util.List;

public interface OAuthClientService {

    public  boolean save(OAuth2RegisteredClient registeredClient);

    public boolean delete(OAuth2RegisteredClient registeredClient);

    public List<OAuth2RegisteredClient> findAll();
}
