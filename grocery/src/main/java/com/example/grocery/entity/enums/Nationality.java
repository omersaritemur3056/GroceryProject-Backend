package com.example.grocery.entity.enums;

public enum Nationality {
    TURKISH("Turkish citizen"),
    OTHER("Other nationality");

    final String text;

    Nationality(String text) {
        this.text = text;
    }
}
