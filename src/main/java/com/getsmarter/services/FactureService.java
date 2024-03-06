package com.getsmarter.services;

import com.getsmarter.entities.Facture;
import com.getsmarter.entities.Paiement;
import com.getsmarter.entities.Student;
import com.getsmarter.repositories.FactureRepo;
import com.getsmarter.repositories.PaiementRepo;
import com.getsmarter.repositories.StudentRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class FactureService {

    private final FactureRepo factureRepo;
    private final StudentService studentService;
    private final StudentRepo studentRepo;
    private final PaiementRepo paiementRepo;
    private final PaiementService paiementService;


    //Methode pour enregistrer une facture
    public void saveFacture(Long studentId, Long paiementID) {

        // Vérification des ID
        if (studentId == null) {
            throw new IllegalArgumentException("Student ID cannot be null");
        }

        if (paiementID == null) {
            throw new IllegalArgumentException("Paiement ID cannot be null");
        }

        Optional<Student> studentOptional = this.studentRepo.findById(studentId);
        Student student = studentOptional.orElseThrow(() -> new EntityNotFoundException("Student not found!"));

        Optional<Paiement> paiementOptional = this.paiementRepo.findById(paiementID);
        Paiement paiement = paiementOptional.orElseThrow(() -> new EntityNotFoundException("Paiement not found!"));


        Facture facture = new Facture();
        //On affecte l'id du student et du paiement dans leur champs respectifs
        facture.setStudent(studentOptional.get());
        facture.setPaiements(paiementOptional.get());

        BigDecimal studentMontantPaye = studentOptional.get().getMontantPaye();
        System.out.println(studentMontantPaye);
        BigDecimal studentMontantRestantaPayer = studentOptional.get().getMontantRestantaPayer();
        System.out.println(studentMontantRestantaPayer);

        if (studentMontantPaye != null && studentMontantRestantaPayer != null) {
            setFactureDetails(facture, student, paiement);
            //Pour calculer le montant restant
            BigDecimal subtrahend = facture.getMontantPaye() != null ? facture.getMontantPaye() : BigDecimal.ZERO;
            facture.setMontantRestant(facture.getMontantTotal().subtract(subtrahend.add(studentMontantPaye)));
            facture.setMontantPayable(facture.getMontantRestant());

            facture.setCreated_at(LocalDateTime.now());
            this.factureRepo.save(facture);

            //On modifie les modalites dans le student
            studentOptional.get().setMontantPaye(facture.getMontantPaye().add(studentMontantPaye));
            studentOptional.get().setMontantRestantaPayer(facture.getMontantPayable());
            this.studentService.updateStudent(studentId, studentOptional.get());


        } else {
            setFactureDetails(facture, student, paiement);
            //Pour calculer le montant restant
            BigDecimal subtrahend = facture.getMontantPaye() != null ? facture.getMontantPaye() : BigDecimal.ZERO;
            facture.setMontantRestant(facture.getMontantTotal().subtract(subtrahend));

            facture.setMontantPayable(facture.getMontantRestant());

            facture.setCreated_at(LocalDateTime.now());
            this.factureRepo.save(facture);

            studentOptional.get().setMontantPaye(facture.getMontantPaye());
            studentOptional.get().setMontantRestantaPayer(facture.getMontantTotal().subtract(facture.getMontantPaye()));
            this.studentService.updateStudent(studentId, studentOptional.get());
        }

    }

    private void setFactureDetails(Facture facture, Student student, Paiement paiement) {
        facture.setNameStudent(student.getFirstname() + " " + student.getLastname());
        facture.setMatricule(student.getMatricule());
        facture.setCentre(student.getCenter().getName());
        facture.setFormation(student.getFormation().getName());
        facture.setDureeFormation(student.getFormation().getDuree().getPeriode());
        facture.setTypeFormation(student.getFormation().getTypeFormation().getHoraire());

        facture.setTypePaiement(paiement.getTypePaiement().getDescription());
        facture.setDatePaiement(paiement.getDatePaiement());
        facture.setMontantTotal(student.getFormation().getPrice());
        facture.setMontantPayable(student.getFormation().getPrice());
        facture.setMontantPaye(paiement.getMontant());
    }
}
