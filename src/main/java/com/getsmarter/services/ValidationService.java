package com.getsmarter.services;

import com.getsmarter.entities.Admin;
import com.getsmarter.entities.Validation;
import com.getsmarter.repositories.ValidationRepo;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Random;



@AllArgsConstructor
@Service
public class ValidationService {

    private final ValidationRepo validationRepo;

    private final NotificationService notificationService;


    public void saveValidation(Admin admin) {
        Validation validation = new Validation();
        validation.setAdmin(admin);

        Instant creation = Instant.now();
        validation.setDateCreation(creation);

        Instant expiration = creation.plus(30, ChronoUnit.MINUTES);
        validation.setExpire(expiration);

        //Creer le code de validation
        Random random = new Random();
        int randomNumber = random.nextInt(999999);
        String code = String.format("%06d", randomNumber);

        validation.setCreated_at(LocalDateTime.now());

        //On l'enregistre dans la base de donnee
        validation.setCode(code);
        this.validationRepo.save(validation);

        //On envoie le mail a l'admin pour qu'il active son compte
        this.notificationService.sendNotification(validation);
    }



    //Methode pour recuperer le code d'activation du compte
    public Validation readByCode(String code) {
        return this.validationRepo.findByCode(code).orElseThrow(() -> new RuntimeException("Votre code est invalide !"));
    }
}
