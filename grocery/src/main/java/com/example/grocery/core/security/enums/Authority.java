package com.example.grocery.core.security.enums;

public enum Authority {

    USER,
    ADMIN;

    public String getAuthority() {
        return name();
    }
}
