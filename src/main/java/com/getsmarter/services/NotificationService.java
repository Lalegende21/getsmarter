package com.getsmarter.services;

import com.getsmarter.entities.Validation;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NotificationService {

    JavaMailSender javaMailSender;

    public void sendNotification(Validation validation) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("delfredtene17@gmail.com");
        mailMessage.setTo(validation.getAdmin().getEmail());
        mailMessage.setSubject("Votre code d'activation !");

        String text = String.format("Salut %s !\n Votre code d'activation est %s et expire dans 1 heure. A bientot !",
                validation.getAdmin().getFirstname() +" "+validation.getAdmin().getLastname(),
                validation.getCode());
        mailMessage.setText(text);
        javaMailSender.send(mailMessage);
    }
}
