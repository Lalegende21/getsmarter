package com.getsmarter.services;

import com.getsmarter.entities.*;
import com.getsmarter.enums.TypeRole;
import com.getsmarter.mails.EmailService;
import com.getsmarter.repositories.ImageRepo;
import com.getsmarter.repositories.JwtRepo;
import com.getsmarter.repositories.RoleRepository;
import com.getsmarter.repositories.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.MailException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private UserRepo userRepo;

    private RoleRepository roleRepository;

    private JwtRepo jwtRepo;

    private ImageService imageService;

    private ImageRepo imageRepo;

    private EmailService emailService;

    private BCryptPasswordEncoder passwordEncoder;

    private ValidationService validationService;



    //Methode pour enregistrer un user
    public void saveUser(User user) {

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
        String cryptPassword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(cryptPassword);

        //On affecte le role a l'user
        Role role = Role.builder()
                .libelle(TypeRole.USER)
                .created_at(LocalDateTime.now())
                .build();
//        role.setCreated_at(LocalDateTime.now());
//        role.setLibelle(TypeRole.ADMIN);

        user.setRole(role);
        //On enregistre la date de creation de l'user
        user.setCreated_at(LocalDateTime.now());



        //On envoie un message de confirmation par mail
        try {
            String email = user.getEmail();
            String subject = "Confirmation d'enregistrement en tant qu'utilisateur de l'application ! " +"\uD83C\uDF89";
            String text = "Félicitations! " + "\uD83C\uDF89" +" Vous êtes désormais un utilisateur de la plateforme. Votre contribution et votre engagement sont précieux. Bienvenue dans l'équipe! " + "\uD83C\uDF1F";
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
    @Transactional(readOnly = true)
    @Cacheable(value = "user")
    public List<User> getAllUser() {
        //Afficher les resultats de la base de donne par ordre decroissant
        Sort sort = Sort.by(Sort.Direction.DESC, "id");

        return this.userRepo.findAll(sort);
    }


    //Methode pour recuperer un user par son id
    @Transactional(readOnly = true)
    @Cacheable(value = "user", key = "#id")
    public User getUserById(Long id) {
        Optional<User> admin = this.userRepo.findById(id);
        return admin.orElseThrow(() -> new RuntimeException("Utilisateur avec l'identifiant: " +id+ " pas trouve!"));
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "user", key = "#id")
    public User getByEmail(String email) {
        Optional<User> user = this.userRepo.findByEmail(email);
        return user.orElseThrow(() -> new EntityNotFoundException("Utilisateur avec l'email: " +email+ " pas trouve!"));
    }


    //Methode pour recuperer les utilisateurs ajoutes recemment (1 derniers jours)
    public List<User> getRecentlyAddedUsers() {
        // Définir la date de début pour récupérer les étudiants ajoutés récemment (par exemple, les 7 derniers jours)
        // Logique pour déterminer la date de début appropriée (1 jours avant la date actuelle)
        LocalDateTime startDate = LocalDate.now().minus(1, ChronoUnit.DAYS).atStartOfDay();

        return this.userRepo.findRecentlyAddedUsers(startDate);
    }


    public User changeRoleUserToAdmin(Long id) {
        Optional<User> userOptional = this.userRepo.findById(id);

        if (userOptional.isEmpty()) {
            throw new EntityNotFoundException("Aucun utilisateur avec cet identifiant trouve !");
        }

        userOptional.get().setRole(
                Role.builder()
                        .libelle(TypeRole.ADMIN)
                        .created_at(LocalDateTime.now())
                        .build());
        this.userRepo.save(userOptional.get());
        return userOptional.get();
    }


    public User changeRoleAdminToUser(Long id) {
        Optional<User> userOptional = this.userRepo.findById(id);

        if (userOptional.isEmpty()) {
            throw new EntityNotFoundException("Aucun utilisateur avec cet identifiant trouve !");
        }

        userOptional.get().setRole(
                Role.builder()
                        .libelle(TypeRole.USER)
                        .created_at(LocalDateTime.now())
                        .build()
        );

        this.userRepo.save(userOptional.get());
        return userOptional.get();
    }



    //Methode pour faire a mise a jour des donnees d'un user
    @Transactional(readOnly = true)
    @CachePut(value = "user", key = "#user.id")
    public void updateUser(Long id, User user) {
        User userUpdate = this.getUserById(id);

        if (userUpdate.getId().equals(user.getId())) {
            userUpdate.setFirstname(user.getFirstname());
            userUpdate.setLastname(user.getLastname());
            userUpdate.setEmail(user.getEmail());
            userUpdate.setPhonenumber(user.getPhonenumber());
            userUpdate.setSexe(user.getSexe());
            userUpdate.setCity(user.getCity());
            userUpdate.setCountry(user.getCountry());
            userUpdate.setDob(user.getDob());

            this.userRepo.save(userUpdate);
        }else {
            throw new RuntimeException("Incoherence entre l'identifiant fourni et l'identifiant de l'utilisateur a modifier!");
        }
    }



    //Methode pour supprimer tous les admins
    @Transactional(readOnly = true)
    @CacheEvict(value = "user", allEntries = true)
    public void deleteAllUser() {
        this.userRepo.deleteAll();
    }



    //Methode pour supprimer es admins par id
    @Transactional(readOnly = true)
    @CacheEvict(value = "user", key = "#id")
    public void deleteUserById(Long id) {
        this.jwtRepo.deleteByUserId(id);
        this.userRepo.deleteById(id);
    }

    public void activationCompte(Map<String, String> activation) {
        Validation validation = this.validationService.readByCode(activation.get("code"));
        //On renvoie une exception si le moment ou il veut valider le compte est supprimer au delai delai d'expiration du code
        if (Instant.now().isAfter(validation.getExpire())) {
            throw new RuntimeException("Votre code a expire !");
        }

        //On recupere l'user a activer
        User user = this.userRepo.findById(validation.getUser().getId()).orElseThrow( () -> new RuntimeException("L'utilisateur dont il faut activer le compte n'a pas ete trouve!"));

        //On envoie un mail pour certifier que le compte a ete active
        try {
            String name = user.getFirstname() + user.getLastname();
            String to = user.getEmail();
            String subject = "Activation Du compte validée !";
            String text = "Félicitations "+name+ " ! \uD83C\uDF89 Votre compte administrateur a été activé avec succès!\n \uD83C\uDF1F Vous pouvez maintenant accéder à toutes les fonctionnalités administratives. \uD83D\uDE80";
            this.emailService.sendEmail(to, subject, text);
        }catch (MailException e) {
            System.out.println(e);
            throw new RuntimeException("Impossible d'envoyer le mail, verifier l'adresse mail du destinataire ou votre connexion internet !");
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

    public void resetPassword(Map<String, String> parametres) {
        User user = (User) this.loadUserByUsername(parametres.get("email"));
        if(user == null) {
            throw new EntityNotFoundException("Aucun utilisateur avec cette email trouve!");
        }
        this.validationService.saveValidation(user);
    }

    public void updatePassword(Map<String, String> parametres) {
        User user = (User) this.loadUserByUsername(parametres.get("email"));
        Validation validation = this.validationService.readByCode(parametres.get("code"));

        if(validation.getUser().getEmail().equals(user.getEmail())) {
            //On crypte le mot de passe
            String cryptPassword = this.passwordEncoder.encode(parametres.get("password"));
            user.setPassword(cryptPassword);
            this.userRepo.save(user);
        }

    }


    @Transactional
    public User saveImageUser(Long id, MultipartFile imageFile) throws IOException {
        // Vérifier la taille du fichier image
        if (imageFile.getSize() > 5 * 1024 * 1024) {
            throw new RuntimeException("Le poids de l'image ne doit pas depasser 5MB.");
        }

        // Définir l'URL de l'image sur l'entité Center
        Optional<User> optionalUser = this.userRepo.findById(id);
        if (optionalUser.isEmpty()) {
            throw new EntityNotFoundException("Aucun etudiant avec cet identifiant trouve !");
        }

        // Vérifier si une image existe déjà pour ce center
        Image existingImage = this.imageRepo.findByUser(optionalUser.get());
        if (existingImage != null) {
            // Supprimer l'image existante
            this.imageRepo.delete(existingImage);
        }

        // Enregistrer la nouvelle image
        Image newImage = this.imageService.uploadImageToFolder(imageFile);
        newImage.setUser(optionalUser.get());
        this.imageRepo.save(newImage);

        // Enregistrer le center avec l'URL de la nouvelle image
        optionalUser.get().setImage(newImage.getFilePath());
        return this.userRepo.save(optionalUser.get());
    }

}
