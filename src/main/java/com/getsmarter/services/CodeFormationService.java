package com.getsmarter.services;

import com.getsmarter.entities.CodeFormation;
import com.getsmarter.repositories.CodeFormationRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CodeFormationService {

    private final CodeFormationRepo codeFormationRepo;


    //Methode pour enregistrer un code
    public void saveCode(CodeFormation codeFormation) {

        Optional<CodeFormation> optionalCodeFormation = this.codeFormationRepo.findByCode(codeFormation.getCode());
        if (optionalCodeFormation.isPresent()) {
            throw new RuntimeException("Ce code existe deja !");
        }
        codeFormation.getCode().toUpperCase();

        codeFormation.setCreated_at(LocalDateTime.now());
        this.codeFormationRepo.save(codeFormation);
    }


    //Methode pour recuperer tous les codes
    public List<CodeFormation> getAllCode() {
        return this.codeFormationRepo.findAll();
    }


    //Methode pour recuperer un code par id
    public CodeFormation getCodeById(Long id) {
        Optional<CodeFormation> optionalCodeFormation = this.codeFormationRepo.findById(id);
        return optionalCodeFormation.orElseThrow(() -> new RuntimeException("Code formation with id: "+id+ " not found"));
    }



    //Methode pour update un code
    public void updateCode(Long id, CodeFormation codeFormation) {
        CodeFormation updateCode = this.getCodeById(id);

        if (updateCode.getId().equals(codeFormation.getId())) {
            updateCode.setCode(codeFormation.getCode());
            updateCode.setFormation(codeFormation.getFormation());
            this.codeFormationRepo.save(updateCode);
        } else {
            throw new RuntimeException("Something went wrong !");
        }
    }



    //Methode pour supprimer tous les codes
    public void deleteAllCode() {
        this.codeFormationRepo.findAll();
    }



    //Methode pour supprimer un code par son id
    public void deleteCodeById(Long id) {
        this.codeFormationRepo.deleteById(id);
    }
}
