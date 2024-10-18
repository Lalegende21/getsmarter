package com.getsmarter.services;

import com.getsmarter.entities.Paiement;
import com.getsmarter.entities.Student;
import com.getsmarter.repositories.PaiementRepo;
import com.getsmarter.repositories.StudentRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PaiementService {

    private final PaiementRepo paiementRepo;
    private final StudentService studentService;

    private final StudentRepo studentRepo;


    public void savePaiement(Paiement paiement) {

        Optional<Student> studentOptional = Optional.ofNullable(this.studentService.getStudentById(paiement.getStudent().getId()));
        if (studentOptional.isEmpty()) {
            throw new EntityNotFoundException("Aucun etudiant avec cet identifiant trouve !");
        }

        if (studentOptional.isPresent()) {
            BigDecimal studentMontantPaye = studentOptional.get().getMontantPaye();
            BigDecimal studentMontantTotal = studentOptional.get().getMontantTotal();
            System.out.println("Student montant total: "+studentMontantTotal);
            System.out.println("student montant paye: " + studentMontantPaye);
            BigDecimal studentMontantRestantaPayer = studentOptional.get().getMontantRestantaPayer();
            System.out.println("student montant restant a payer: " + studentMontantRestantaPayer);

            if (studentMontantPaye != null && studentMontantRestantaPayer != null) {

                //Pour calculer le montant restant
                BigDecimal subtrahend = paiement.getMontant() != null ? paiement.getMontant() : BigDecimal.ZERO;
                BigDecimal montantRestantaPayer = studentMontantTotal.subtract(subtrahend.add(studentMontantPaye));

                //On modifie les modalites dans le student
                studentOptional.get().setMontantPaye(paiement.getMontant().add(studentMontantPaye));
                studentOptional.get().setMontantRestantaPayer(montantRestantaPayer);

                //On compare si le montant paye est deja egal ou superieur au montant total a paye
                int comparaison = studentOptional.get().getMontantPaye().compareTo(studentMontantTotal);

                if(comparaison > 0) {
                    throw new RuntimeException("Le montant paye est superieur au montant total a payer!");
                } else if(comparaison < 0) {
                    this.studentRepo.save(studentOptional.get());
                }
                else {
                    System.out.println("Felicitation vous avez integralement paye vos frais de formations!");
                    this.studentRepo.save(studentOptional.get());
                }

                paiement.setCreated_at(LocalDateTime.now());
                this.paiementRepo.save(paiement);

            } else {
                //Pour calculer le montant restant
                BigDecimal subtrahend = paiement.getMontant() != null ? paiement.getMontant() : BigDecimal.ZERO;

                studentOptional.get().setMontantPaye(paiement.getMontant());
                studentOptional.get().setMontantRestantaPayer(studentMontantTotal.subtract(paiement.getMontant()));

                System.out.println("Montant paye: " + studentOptional.get().getMontantPaye());
                System.out.println("Montant restant a payer: " + studentOptional.get().getMontantRestantaPayer());
                this.studentRepo.save(studentOptional.get());

                paiement.setCreated_at(LocalDateTime.now());
                this.paiementRepo.save(paiement);

            }

        }
    }





    //Methode pour recuperer tous les paiements
    @Transactional(readOnly = true)
    @Cacheable(value = "paiement")
    public List<Paiement> getAllPaiement() {
        //Afficher les resultats de la base de donne par ordre decroissant
        Sort sort = Sort.by(Sort.Direction.DESC, "id");

        return this.paiementRepo.findAll(sort);
    }


    //Methode pour recuperer les etudiants ajoutes recemment (1 derniers jours)
    public List<Paiement> getRecentlyAddedPaiement() {
        // Définir la date de début pour récupérer les étudiants ajoutés récemment (par exemple, les 7 derniers jours)
        // Logique pour déterminer la date de début appropriée (1 jours avant la date actuelle)
        LocalDateTime startDate = LocalDate.now().minus(1, ChronoUnit.DAYS).atStartOfDay();

        return this.paiementRepo.findRecentlyAddedPaiements(startDate);
    }


    //Methode pour recuperer un paiement par son id
    @Transactional(readOnly = true)
    @Cacheable(value = "paiement", key = "#id")
    public Paiement getPaiementById(Long id) {
        Optional<Paiement> optionalPaiement = this.paiementRepo.findById(id);
        return optionalPaiement.orElseThrow(() -> new EntityNotFoundException("Paiement with id: " +id+ " not found !"));
    }


    //Methode pour update un paiement
    @Transactional
    @CachePut(value = "paiement", key = "#paiement.id")
    public void updatePaiement(Long id, Paiement paiement) {
        Paiement updatePaiement = this.getPaiementById(id);

        if (updatePaiement.getId().equals(paiement.getId())) {
            updatePaiement.setMontant(paiement.getMontant());
            updatePaiement.setTypePaiement(paiement.getTypePaiement());
            updatePaiement.setStudent(paiement.getStudent());

            this.paiementRepo.save(updatePaiement);
        }else {
            throw new RuntimeException("Something went wrong !");
        }
    }



    //Methode pour supprimer tous les paiements
    @Transactional
    @CacheEvict(value = "paiement", allEntries = true)
    public void deleteAllPaiement() {
        this.paiementRepo.deleteAll();
    }


    //Methode pour supprimer un paiement par id
    @Transactional
    @CacheEvict(value = "paiement", key = "#id")
    public void deletepaiementById(Long id) {
        this.paiementRepo.deleteById(id);
    }
}
