package com.example.security.dto;

import lombok.Data;

import java.io.Serializable;


@Data
public class AuthenticationResponse implements Serializable {


    private String token;
    private String refresh;

    public AuthenticationResponse(String token) {
        this.token = token;

        this.refresh=null;
    }

    public AuthenticationResponse(String token, String refresh) {
        this.token = token;
        this.refresh = refresh;
    }
}
