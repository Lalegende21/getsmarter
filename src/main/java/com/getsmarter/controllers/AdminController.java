package com.getsmarter.controllers;

import com.getsmarter.dto.AuthentificationDto;
import com.getsmarter.entities.Admin;
import com.getsmarter.security.JwtService;
import com.getsmarter.services.AdminService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(path = "/admin")
public class AdminController {

    private final AdminService adminService;

    private final AuthenticationManager authenticationManager;

    private JwtService jwtService;


    //Methode pour enregistrer un admin
    @PostMapping(path = "/save-admin")
    public ResponseEntity<String> saveAdmin(@RequestBody Admin admin) {
        try {
            this.adminService.saveAdmin(admin);
            return new ResponseEntity<>("Admin register successfully !", HttpStatus.CREATED);
        }catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Something went wrong: "+ e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    //Methode pour activer le compte
    @PostMapping(path = "/activation-admin")
    public ResponseEntity<String> activateCompteAdmin(@RequestBody Map<String, String> activation) {
        try {
            this.adminService.activationCompte(activation);
            return new ResponseEntity<>("Compte activate successfully !", HttpStatus.CREATED);
        }catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Something went wrong: "+ e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    //Methode pour se connecter
    @PostMapping(path = "/connexion-admin")
    public Map<String, String> connexionCompteAdmin(@RequestBody AuthentificationDto authentificationDto) {
        try {
            final Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authentificationDto.username(), authentificationDto.password())
            );
            //Si l'utilisateur est authentifier, on genere un jwt
            if (authenticate.isAuthenticated()) {
                return this.jwtService.generate(authentificationDto.username());
            }
        }catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }




    //Methode pour recuperer tous les admins
    @GetMapping(path = "/get-all-admin")
    public List<Admin> getAllAdmin() {
        return this.adminService.getAllAdmin();
    }



    //Methode pour recuperer un admin grace a son id
    @GetMapping(path = "/get-admin/{id}")
    public Admin getAdminById(@PathVariable Long id) {
        try {
            return this.adminService.getAdminById(id);
        }catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }



    //Methode pour update un admin
    @PutMapping(path = "/update-admin/{id}")
    public ResponseEntity<String> updateAdmin(@PathVariable Long id, @RequestBody Admin admin) {
        try {
            this.adminService.updateAdmin(id, admin);
            return new ResponseEntity<>("Admin was successfully update !", HttpStatus.ACCEPTED);
        }catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Something went wrong: "+ e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    //Methode pour supprimer tous les admins
    @DeleteMapping(path = "/delete-all")
    public ResponseEntity<String> deleteAllAdmin() {
        try {
            this.adminService.deleteAllAdmin();
            return new ResponseEntity<>("All admin was successfully delete !", HttpStatus.OK);
        }catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Something went wrong: " +e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    //Methode pour supprimer un admin par son id
    @DeleteMapping(path = "/delete-admin/{id}")
    public ResponseEntity<String> deleteAdminById(@PathVariable Long id) {
        try {
            this.adminService.deleteAdminById(id);
            return new ResponseEntity<>("Admin delete successfully !", HttpStatus.OK);
        }catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Something went wrong: " +e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
