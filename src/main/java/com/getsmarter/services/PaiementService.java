package com.getsmarter.services;

import com.getsmarter.entities.Facture;
import com.getsmarter.entities.Paiement;
import com.getsmarter.entities.Student;
import com.getsmarter.entities.TypePaiement;
import com.getsmarter.repositories.FactureRepo;
import com.getsmarter.repositories.PaiementRepo;
import com.getsmarter.repositories.StudentRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PaiementService {

    private final PaiementRepo paiementRepo;
    private final FactureRepo factureRepo;
    private final StudentService studentService;

    private final TypePaiementService typePaiementService;


    //Methode pour enregistrer un paiement
    public Paiement savePaiement(Paiement paiement) {

        LocalDateTime time = LocalDateTime.now();
        BigDecimal montantPaye = paiement.getMontant();

        Long idTypePaiement = paiement.getTypePaiement().getId();

        TypePaiement typePaiement = this.typePaiementService.getTypePaiementById(paiement.getTypePaiement().getId());
        String typePaiementStudent = typePaiement.getDescription();

        paiement.setName(paiement.getName().toUpperCase());
        paiement.setDatePaiement(time);
        paiement.setCreated_at(LocalDateTime.now());


        //Je recupere l'id du student pour avoir ses infos
        Long id = paiement.getStudent().getId();
        Student student = this.studentService.getStudentById(id);

        if (student == null) {
            throw new RuntimeException("Studen not found !");
        }

        return this.paiementRepo.save(paiement);
    }


    //Methode pour recuperer tous les paiements
    public List<Paiement> getAllPaiement() {
        return this.paiementRepo.findAll();
    }


    //Methode pour recuperer un paiement par son id
    public Paiement getPaiementById(Long id) {
        Optional<Paiement> optionalPaiement = this.paiementRepo.findById(id);
        return optionalPaiement.orElseThrow(() -> new RuntimeException("Paiement with id: " +id+ " not found !"));
    }


    //Methode pour update un paiement
    public void updatePaiement(Long id, Paiement paiement) {
        Paiement updatePaiement = this.getPaiementById(id);

        if (updatePaiement.getId().equals(paiement.getId())) {
            updatePaiement.setName(paiement.getName());
            updatePaiement.setMontant(paiement.getMontant());
            updatePaiement.setDatePaiement(paiement.getDatePaiement());
            updatePaiement.setTypePaiement(paiement.getTypePaiement());
        }
    }



    //Methode pour supprimer tous les paiements
    public void deleteAllPaiement() {
        this.paiementRepo.deleteAll();
    }


    //Methode pour supprimer un paiement par id
    public void deletepaiementById(Long id) {
        this.paiementRepo.deleteById(id);
    }
}
