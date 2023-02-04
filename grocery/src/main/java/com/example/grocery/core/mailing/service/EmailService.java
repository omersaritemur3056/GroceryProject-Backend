package com.example.grocery.core.mailing.service;

import com.example.grocery.core.mailing.model.Email;
import com.example.grocery.core.utilities.results.Result;

public interface EmailService {

    public void sendEmail(String email);

    public void sendSimpleMessage(Email email);

    public Result sendActivationEmail(String email);
}
