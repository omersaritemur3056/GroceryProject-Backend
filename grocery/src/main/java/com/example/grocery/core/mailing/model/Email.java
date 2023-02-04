package com.example.grocery.core.mailing.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Email {

    private String emailFrom;

    private String emailTo;

    private String subject;

    private String content;
}
