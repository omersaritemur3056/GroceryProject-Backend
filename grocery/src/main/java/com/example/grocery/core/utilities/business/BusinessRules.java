package com.example.grocery.core.utilities.business;

import com.example.grocery.core.utilities.results.Result;

public class BusinessRules {

    public static Result run(Result... logics) {
        for (Result logic : logics) {
            if (!logic.isSuccess()) {
                return logic;
            }
        }
        return null;
    }
}
