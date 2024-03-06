package com.getsmarter.mails;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/mail")
public class EmailController {

    private EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping(path = "/send-mail")
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequest emailRequest){
        try {
            String to = emailRequest.getTo();
            String subject = emailRequest.getSubject();
            String text = emailRequest.getText();
            this.emailService.sendEmail(to, subject, text);
            return new ResponseEntity<>("Mail sent successfully!", HttpStatus.OK);
        }catch (Exception e){
            System.out.println(e);
            return new ResponseEntity<>("Something went wrong: "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
