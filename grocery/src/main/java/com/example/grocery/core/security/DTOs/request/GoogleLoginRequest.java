package com.example.grocery.core.security.DTOs.request;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class GoogleLoginRequest {

    private String id;

    private String idToken;

    private String name;

    private String email;

    private String firstName;

    private String lastName;

    private String photoUrl;

    private String provider;
}
