package com.example.grocery.core.mailing.helper;

import java.security.SecureRandom;

public class ValidationCodeGenerator {

    private ValidationCodeGenerator() {
    }

    public static int generateSixDigitRandomNumber() {
        SecureRandom secureRandom = new SecureRandom();
        return secureRandom.nextInt(1000000 - 100000) + 100000;
    }
}
