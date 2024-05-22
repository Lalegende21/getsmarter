package com.getsmarter.services;

import com.getsmarter.entities.User;
import com.getsmarter.entities.Role;
import com.getsmarter.entities.Validation;
import com.getsmarter.enums.TypeRole;
import com.getsmarter.mails.EmailService;
import com.getsmarter.repositories.RoleRepository;
import com.getsmarter.repositories.UserRepo;
import com.getsmarter.response.UserResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.mail.MailException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private UserRepo userRepo;

    private RoleRepository roleRepository;

    private ImageService imageService;

    private EmailService emailService;

//    private BCryptPasswordEncoder passwordEncoder;

    private ValidationService validationService;



    //Methode pour enregistrer un user
    public void saveAdmin(User user) {

        // On verifie si l'email contient le symbole @
        if (!user.getEmail().contains("@")){
            throw new RuntimeException("Votre email est invalide!");
        }

        //On verifie si l'email contient un .
        if (!user.getEmail().contains(".")){
            throw new RuntimeException("Votre email est invalide!");
        }

        //On verifie si un utilisateur avec l'email donnee existe deja
        Optional<User> optionalAdmin = this.userRepo.findByEmail(user.getEmail());
        if (optionalAdmin.isPresent()){
            throw new RuntimeException("Votre email est déjà utilisée!");
        }

        //On verifie si un utilisareur avec le numero de telephone existe deja
        Optional<User> optionalAdminPhoneNumber = this.userRepo.findByPhonenumber(user.getPhonenumber());
        if (optionalAdminPhoneNumber.isPresent()) {
            throw new RuntimeException("Votre numero de telephone est déjà utilisée!");
        }

        //On crypte le mot de passe
//        String cryptPassword = this.passwordEncoder.encode(user.getPassword());
//        user.setPassword(cryptPassword);

        //On affecte le role a l'user
        Role role = new Role();
        role.setCreated_at(LocalDateTime.now());
        role.setLibelle(TypeRole.ADMIN);

        user.setRole(role);
        //On enregistre la date de creation de l'user
        user.setCreated_at(LocalDateTime.now());



        //On envoie un message de confirmation par mail
        try {
            String email = user.getEmail();
            String subject = "Confirmation d'enregistrement en tant qu'administrateur ! " +"\uD83C\uDF89";
            String text = "Félicitations! " + "\uD83C\uDF89" +" Vous êtes désormais administrateur de la plateforme. Votre contribution et votre engagement sont précieux. Bienvenue dans l'équipe! " + "\uD83C\uDF1F";
            this.emailService.sendEmail(email, subject, text);
        }catch (MailException e) {
            System.out.println(e);
            throw new RuntimeException("Impossible d'envoyer le mail, verifier votre connexion internet !");
        }


        //On enregistre l'user
        user = this.userRepo.save(user);


        //On l'envoie le code de validation de compte
        this.validationService.saveValidation(user);
    }


    //Methode pour tous les admins
    public List<User> getAllAdmin() {
        //Afficher les resultats de la base de donne par ordre decroissant
        Sort sort = Sort.by(Sort.Direction.DESC, "id");

        return this.userRepo.findAll(sort);
    }


    //Methode pour recuperer un user par son id
    public User getAdminById(Long id) {
        Optional<User> admin = this.userRepo.findById(id);
        return admin.orElseThrow(() -> new RuntimeException("Utilisateur avec l'identifiant: " +id+ " pas trouve!"));
    }

    public User getByEmail(String email) {
        Optional<User> user = this.userRepo.findByEmail(email);
        return user.orElseThrow(() -> new EntityNotFoundException("Utilisateur avec l'email: " +email+ " pas trouve!"));
    }



    //Methode pour faire a mise a jour des donnees d'un user
    public void updateAdmin(Long id, User user) {
        User userUpdate = this.getAdminById(id);

        if (userUpdate.getId().equals(user.getId())) {
            userUpdate.setFirstname(user.getFirstname());
            userUpdate.setLastname(user.getLastname());
            userUpdate.setEmail(user.getEmail());
            userUpdate.setPassword(user.getPassword());
            userUpdate.setPhonenumber(user.getPhonenumber());
            userUpdate.setSexe(user.getSexe());

            this.userRepo.save(userUpdate);
        } else {
            throw new RuntimeException("Incoherence entre l'id fourni et l'id de l'utilisateur a modifie!");
        }
    }



    //Methode pour supprimer tous les admins
    public void deleteAllAdmin() {
        this.userRepo.deleteAll();
    }



    //Methode pour supprimer es admins par id
    public void deleteAdminById(Long id) {
        this.userRepo.deleteById(id);
    }

    public void activationCompte(Map<String, String> activation) {
        Validation validation = this.validationService.readByCode(activation.get("code"));
        //On renvoie une exception si le moment ou il veut valider le compte est supprimer au delai delai d'expiration du code
        if (Instant.now().isAfter(validation.getExpire())) {
            throw new RuntimeException("Votre code a expire !");
        }

        //On recupere l'user a activer
        User user = this.userRepo.findById(validation.getUser().getId()).orElseThrow( () -> new RuntimeException("Utilisateur pas trouve!"));

        //On envoie un mail pour certifier que le compte a ete active
        try {
            String name = user.getFirstname() + user.getLastname();
            String to = user.getEmail();
            String subject = "Activation Du compte validée !";
            String text = "Félicitations "+name+ " ! \uD83C\uDF89 Votre compte administrateur a été activé avec succès!\n \uD83C\uDF1F Vous pouvez maintenant accéder à toutes les fonctionnalités administratives. \uD83D\uDE80";
            this.emailService.sendEmail(to, subject, text);
        }catch (MailException e) {
            System.out.println(e);
            throw new RuntimeException("Impossible d'envoyer le mail, verifier votre connexion internet !");
        }

        //On active le compte user
        user.setActif(true);
        this.userRepo.save(user);
    }


    //Methode pour chercher l'utilisateur dans la base de donnee en fonction du mail pour comparer les mots de passe
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepo
                .findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Aucun utilisateur trouve avec cet email!"));
    }


    //Methode pour renitialiser le mot de passe
//    public void resetPassword(Map<String, String> parameters) {
//        User user = (User) this.loadUserByUsername(parameters.get("email"));
//        if(user == null) {
//            throw new EntityNotFoundException("Aucun utilisateur avec cette email trouve!");
//        }
//        this.validationService.saveValidation(user);
//    }


    //Methode pour modifier le mot de passe
//    public void updatePassword(Map<String, String> parameters) {
//        User user = (User) this.loadUserByUsername(parameters.get("email"));
//        Validation validation = validationService.readByCode(parameters.get("code"));
//
//        if (validation.getUser().getEmail().equals(user.getEmail())) {
//            //On crypte le mot de passe
//            String cryptPassword = this.passwordEncoder.encode(parameters.get("password"));
//            user.setPassword(cryptPassword);
//            this.userRepo.save(user);
//        }
//    }
}
