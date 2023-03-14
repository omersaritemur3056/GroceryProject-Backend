package com.example.grocery.model.enums;

public enum Nationality {
    TURKISH("Turkish citizen"),
    OTHER("Other nationality");

    final String text;

    Nationality(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
