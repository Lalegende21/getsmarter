package com.getsmarter.services;

import com.getsmarter.entities.SpecificiteFormation;
import com.getsmarter.repositories.CodeFormationRepo;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SpecificiteFormationService {

    private final CodeFormationRepo codeFormationRepo;


    //Methode pour enregistrer un code
    public void saveCode(SpecificiteFormation specificiteFormation) {

        Optional<SpecificiteFormation> optionalCodeFormation = this.codeFormationRepo.findByCode(specificiteFormation.getCode());
        if (optionalCodeFormation.isPresent()) {
            throw new RuntimeException("Ce code existe deja !");
        }
        specificiteFormation.getCode().toUpperCase();

        specificiteFormation.setCreated_at(LocalDateTime.now());
        this.codeFormationRepo.save(specificiteFormation);
    }


    //Methode pour recuperer tous les codes
    @Transactional(readOnly = true)
    @Cacheable(value = "specificite")
    public List<SpecificiteFormation> getAllCode() {
        //Afficher les resultats de la base de donne par ordre decroissant
        Sort sort = Sort.by(Sort.Direction.DESC, "id");

        return this.codeFormationRepo.findAll(sort);
    }


    @Transactional(readOnly = true)
    @Cacheable(value = "specificte", key = "#id")
    //Methode pour recuperer un code par id
    public SpecificiteFormation getCodeById(Long id) {
        Optional<SpecificiteFormation> optionalCodeFormation = this.codeFormationRepo.findById(id);
        return optionalCodeFormation.orElseThrow(() -> new RuntimeException("Aucune specificite avec cet identifiant: "+id+ " trouvee!"));
    }



    //Methode pour update un code
    @Transactional
    @CachePut(value = "specificte", key = "#specificite.id")
    public void updateCode(Long id, SpecificiteFormation specificiteFormation) {
        SpecificiteFormation updateCode = this.getCodeById(id);

        if (updateCode.getId().equals(specificiteFormation.getId())) {
            updateCode.setCode(specificiteFormation.getCode());
            updateCode.setLibelle(specificiteFormation.getLibelle());
            updateCode.setFormation(specificiteFormation.getFormation());
            this.codeFormationRepo.save(updateCode);
        } else {
            throw new RuntimeException("Incoherence entre l'id fourni et l'id de la specificite de la formation a modifie !");
        }
    }



    //Methode pour supprimer tous les codes
    @Transactional
    @CacheEvict(value = "specificite", allEntries = true)
    public void deleteAllCode() {
        this.codeFormationRepo.findAll();
    }



    //Methode pour supprimer un code par son id
    @Transactional
    @CacheEvict(value = "specificite", key = "#id")
    public void deleteCodeById(Long id) {
        this.codeFormationRepo.deleteById(id);
    }
}
