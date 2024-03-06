package com.getsmarter.controllers;

import com.getsmarter.entities.Image;
import com.getsmarter.services.ImageService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/image")
public class ImageController {

    private final ImageService imageService;


    //Methode pour enregistrer une image
    @PostMapping(path = "/save-image")
    public ResponseEntity<String> uploadImageToFolder(@RequestParam("image") MultipartFile file) {
        try {
            String uploadImage = String.valueOf(imageService.uploadImageToFolder(file));
            return ResponseEntity.status(HttpStatus.CREATED).body(uploadImage);
        }catch (Exception e){
            System.out.println(e);
            return new ResponseEntity<>("Something went wrong: "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    //Methode pour lire une image dans la base de donnee
    @GetMapping(path = "/donwload/{fileName}")
    public ResponseEntity<?> donwloadImageFromFolder(@PathVariable String fileName) {
        try {
            byte[] imageData = imageService.donwloadImageFomFolder(fileName);
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.valueOf("image/png"))
                    .body(imageData);
        }catch (Exception e){
            System.out.println(e);
            return new ResponseEntity<>("Something went wrong: "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    //Methode pour faire la mise a jour de l'image
    @PutMapping(path = "/update/{fileId}")
    public ResponseEntity<String> updateImageInFolder(@PathVariable("fileId") Long fileId, @RequestParam("image") MultipartFile file) {
        try {
            String updateImage = imageService.updateImageInFolder(file, fileId);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(updateImage);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Something went wrong: "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    //Methode pour supprimer toutes les images dans le dossier et la base de donnee
    @DeleteMapping(path = "/delete/all")
    public ResponseEntity<String> deleteAllImages() {
        try {
            imageService.deleteAllImages();
            return ResponseEntity.status(HttpStatus.OK).body("All images deleted successfully.");
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Something went wrong: "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    //Methode pour supprimer toutes les images dans le dossier et la base de donnee
    @DeleteMapping(path = "/delete/{fileId}")
    public ResponseEntity<String> deleteImageById(@PathVariable("fileId") Long fileId) {
        try {
            imageService.deleteImageById(fileId);
            return ResponseEntity.status(HttpStatus.OK).body("Image deleted successfully.");
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Something went wrong: "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
