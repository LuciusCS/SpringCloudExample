package com.example.auth.oauth2.client.repository;

import com.example.auth.bean.OAuth2RegisteredClient;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface OAuth2ClientRepository extends JpaRepository<OAuth2RegisteredClient, Long> {
    Optional<OAuth2RegisteredClient> findByClientId(String clientId);
}
