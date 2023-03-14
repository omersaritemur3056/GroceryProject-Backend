package com.example.grocery.model.enums;

public enum Gender {
    UNSPECIFY("unspecify"),
    OTHER("other"),
    MALE("male"),
    FEMALE("female");

    final String text;

    Gender(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
