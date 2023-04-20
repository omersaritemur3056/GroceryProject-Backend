package com.example.grocery.core.security.DTOs.request;

import lombok.Data;

@Data
public class FacebookLoginRequest {
    private String id;

    private String authToken;

    private String name;

    private String email;

    private String firstName;

    private String lastName;

    private String photoUrl;

    private String provider;
}
