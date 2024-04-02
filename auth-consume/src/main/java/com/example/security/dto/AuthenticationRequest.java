package com.example.security.dto;


import lombok.Data;

import java.io.Serializable;

@Data
public class AuthenticationRequest implements Serializable {
    private static final long serialVersionUID = 5926468583005150707L;

    private String username;
    private String password;

    public AuthenticationRequest() {
    }

    public AuthenticationRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
