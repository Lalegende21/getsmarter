package com.getsmarter.mails;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class EmailScheduler {

    private final JavaMailSender javaMailSender;

    public EmailScheduler(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }


    private String to;
    private String subject;
    private String text;


    //@Scheduled(cron = "0 */5 * * * *") // Exécution toutes les 5 minutes
    //@Scheduled(cron = "0 */1 * * * *") // Exécuté toutes les 1 minutes
    @Scheduled(cron = "0 0 0 * * MON") // Exécution chaque semaine le lundi à minuit
    public String sendWeeklyEmail() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        javaMailSender.send(message);
        return "Mail sent successfully!";
    }


    // Ajout des méthodes pour définir les valeurs des variables d'instance
    public void setTo(String to) {
        this.to = to;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setText(String text) {
        this.text = text;
    }
}
