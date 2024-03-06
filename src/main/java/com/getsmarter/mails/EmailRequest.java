package com.getsmarter.mails;

import lombok.Data;

@Data
public class EmailRequest {

    private String to;

    private String subject;

    private String text;
}
