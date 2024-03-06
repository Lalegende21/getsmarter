package com.getsmarter.services;

import com.getsmarter.entities.Admin;
import com.getsmarter.entities.Role;
import com.getsmarter.entities.Validation;
import com.getsmarter.enums.TypeRole;
import com.getsmarter.mails.EmailService;
import com.getsmarter.repositories.AdminRepo;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AdminService implements UserDetailsService {

    private AdminRepo adminRepo;

    private ImageService imageService;

    private EmailService emailService;

    private BCryptPasswordEncoder passwordEncoder;

    private ValidationService validationService;



    //Methode pour enregistrer un admin
    public void saveAdmin(Admin admin) {

        // On verifie si l'email contient le symbole @
        if (!admin.getEmail().contains("@")){
            throw new RuntimeException("Votre email est invalide!");
        }

        //On verifie si l'email contient un .
        if (!admin.getEmail().contains(".")){
            throw new RuntimeException("Votre email est invalide!");
        }

        //On verifie si un utilisateur avec l'email donnee existe deja
        Optional<Admin> optionalAdmin = this.adminRepo.findByEmail(admin.getEmail());
        if (optionalAdmin.isPresent()){
            throw new RuntimeException("Votre email est déjà utilisée!");
        }

        //On verifie si un utilisareur avec le numero de telephone existe deja
        Optional<Admin> optionalAdminPhoneNumber = this.adminRepo.findByPhonenumber(admin.getPhonenumber());
        if (optionalAdminPhoneNumber.isPresent()) {
            throw new RuntimeException("Votre numero de telephone est déjà utilisée!");
        }

        //On crypte le mot de passe
        String cryptPassword = this.passwordEncoder.encode(admin.getPassword());
        admin.setPassword(cryptPassword);

        //On affecte le role a l'admin
        Role role = new Role();
        role.setLibelle(TypeRole.ADMIN);
        admin.setRole(role);

        //On enregistre la date de creation de l'admin
        admin.setCreated_at(LocalDateTime.now());



        //On enregistre l'admin
        admin = this.adminRepo.save(admin);

        //On envoie un message de confirmation par mail
        String email = admin.getEmail();
        String subject = "Confirmation d'enregistrement en tant qu'administrateur ! " +"\uD83C\uDF89";
        String text = "Félicitations! " + "\uD83C\uDF89" +" Vous êtes désormais administrateur de la plateforme. Votre contribution et votre engagement sont précieux. Bienvenue dans l'équipe! " + "\uD83C\uDF1F";
        this.emailService.sendEmail(email, subject, text);


        //On l'envoie le code de validation de compte
        this.validationService.saveValidation(admin);
    }




    //Methode pour tous les admins
    public List<Admin> getAllAdmin() {
        return this.adminRepo.findAll();
    }


    //Methode pour recuperer un admin par son id
    public Admin getAdminById(Long id) {
        Optional<Admin> admin = this.adminRepo.findById(id);
        return admin.orElseThrow(() -> new RuntimeException("Admin with id: " +id+ " not found!"));
    }



    //Methode pour faire a mise a jour des donnees d'un admin
    public void updateAdmin(Long id, Admin admin) {
        Admin adminUpdate = this.getAdminById(id);

        if (adminUpdate.getId().equals(admin.getId())) {
            adminUpdate.setFirstname(admin.getFirstname());
            adminUpdate.setLastname(admin.getLastname());
            adminUpdate.setEmail(admin.getEmail());
            adminUpdate.setPassword(admin.getPassword());
            adminUpdate.setPhonenumber(admin.getPhonenumber());
            adminUpdate.setSexe(admin.getSexe());

            this.adminRepo.save(adminUpdate);
        } else {
            throw new RuntimeException("Something went wrong !");
        }
    }



    //Methode pour supprimer tous les admins
    public void deleteAllAdmin() {
        this.adminRepo.deleteAll();
    }



    //Methode pour supprimer es admins par id
    public void deleteAdminById(Long id) {
        this.adminRepo.deleteById(id);
    }

    public void activationCompte(Map<String, String> activation) {
        Validation validation = this.validationService.readByCode(activation.get("code"));
        //On renvoie une exception si le moment ou il veut valider le compte est supprimer au delai delai d'expiration du code
        if (Instant.now().isAfter(validation.getExpire())) {
            throw new RuntimeException("Votre code a expire !");
        }

        //On recupere l'admin a activer
        Admin admin = this.adminRepo.findById(validation.getAdmin().getId()).orElseThrow( () -> new RuntimeException("Admin not found !"));

        //On active le compte admin
        admin.setActif(true);
        this.adminRepo.save(admin);
    }


    //Methode pour chercher l'utilisateur dans la base de donnee en fonction du mail pour comparer les mots de passe
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.adminRepo
                .findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found with this identifiant !"));
    }
}
