package com.example.grocery.core.utilities.business;

import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.core.utilities.results.SuccessResult;

public class BusinessRules {

    private BusinessRules() {
    }

    public static Result run(Result... logics) {
        for (Result logic : logics) {
            if (!logic.isSuccess()) {
                return logic;
            }
        }
        return new SuccessResult();
    }
}
