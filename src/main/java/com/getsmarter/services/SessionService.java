package com.getsmarter.services;

import com.getsmarter.entities.Center;
import com.getsmarter.entities.Formation;
import com.getsmarter.entities.Session;
import com.getsmarter.entities.Student;
import com.getsmarter.repositories.SessionRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SessionService {

    private final SessionRepo sessionRepo;

    public void saveSession(Session session) {
        session.setCreated_at(LocalDateTime.now());
        this.sessionRepo.save(session);
    }


    @Transactional(readOnly = true)
    @Cacheable(value = "session")
    public List<Session> getAllSession() {
        //Afficher les resultats de la base de donne par ordre decroissant
        Sort sort = Sort.by(Sort.Direction.DESC, "id");

        return this.sessionRepo.findAll(sort);
    }



    //Methode pour recuperer les sessions ajoutes recemment (1 derniers jours)
    public List<Session> getRecentlyAddedSessions() {
        // Définir la date de début pour récupérer les étudiants ajoutés récemment (par exemple, les 7 derniers jours)
        // Logique pour déterminer la date de début appropriée (1 jours avant la date actuelle)
        LocalDateTime startDate = LocalDate.now().minus(1, ChronoUnit.DAYS).atStartOfDay();

        return this.sessionRepo.findRecentlyAddedSessions(startDate);
    }


    @Transactional(readOnly = true)
    @Cacheable(value = "session", key = "#id")
    public Session getSessionById(Long id) {
        Optional<Session> optionalSession = this.sessionRepo.findById(id);
        return optionalSession.orElseThrow(() -> new RuntimeException("Aucune session avec cet identifiant trouvee !"));
    }


    @Transactional
    @CachePut(value = "session", key = "#session.id")
    public void updateSession(Long id, Session session) {
        Session updateSession = this.getSessionById(id);

        if (updateSession.getId().equals(session.getId())) {
            updateSession.setDateDebut(session.getDateDebut());
            updateSession.setStudents(session.getStudents());
            this.sessionRepo.save(updateSession);
        } else {
            throw new RuntimeException("Incoherence entre l'id fourni et l'id de la session a modifie !");
        }
    }

    @Transactional
    @CacheEvict(value = "session", allEntries = true)
    public void deleteAllSession() {
        this.sessionRepo.deleteAll();
    }


    @Transactional
    @CacheEvict(value = "session", key = "#id")
    public void deleteSessionById(Long id) {
        this.sessionRepo.deleteById(id);
    }
}
