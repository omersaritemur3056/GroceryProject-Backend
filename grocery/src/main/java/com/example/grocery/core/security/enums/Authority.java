package com.example.grocery.core.security.enums;

public enum Authority {

    USER("user"),
    MODERATOR("moderator"),
    EDITOR("editor"),
    ADMIN("admin"),
    SUPERUSER("superuser");

    final String text;

    Authority(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
