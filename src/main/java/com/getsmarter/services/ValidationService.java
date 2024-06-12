package com.getsmarter.services;

import com.getsmarter.entities.User;
import com.getsmarter.entities.Validation;
import com.getsmarter.repositories.ValidationRepo;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;


@Transactional
@AllArgsConstructor
@Slf4j
@Service
public class ValidationService {

    private final ValidationRepo validationRepo;

    private final NotificationService notificationService;


    public void saveValidation(User user) {
        Validation validation = new Validation();
        validation.setUser(user);

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
        System.out.println("Code enregistre "+code);

        //On envoie le mail a l'user pour qu'il active son compte
        this.notificationService.sendNotification(validation);
    }



    //Methode pour recuperer le code d'activation du compte
    public Validation readByCode(String code) {
        return this.validationRepo.findByCode(code).orElseThrow(() -> new RuntimeException("Votre code est invalide !"));
    }


    //Methode pour nettoyer la base de donnee
//    @Scheduled(cron = "@daily")
//    @Scheduled(cron = "0 */1 * * * *")     //On nettoie la table jwt toutes les 30min dans la base de donnee
//    public void removeUselessJwt() {
//        log.info("Suppression des user dans la table validations à ¨{}", Instant.now());
//        this.validationRepo.deleteAllByExpirationBefore(Instant.now());
//    }
}
