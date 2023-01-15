package com.example.grocery.entity.enums;

public enum OrderStatus {
    ORDER_TAKEN("order taken"),
    GETTING_READY("getting ready"),
    TRANSPORT("transport"),
    WAS_DELIVERED("was delivered");

    final String text;

    OrderStatus(String text) {
        this.text = text;
    }
}
