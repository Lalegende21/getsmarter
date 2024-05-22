package com.getsmarter.controllers;

import com.getsmarter.entities.Image;
import com.getsmarter.response.UploadImageResult;
import com.getsmarter.services.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200",
        allowedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE},
        maxAge = 3600)
@RequestMapping(path = "/image")
public class ImageController {

    private final ImageService imageService;


    //Methode pour enregistrer une image
    @PostMapping(path = "/save-image")
    public ResponseEntity<?> uploadImageForAdminToFolder(@RequestParam("image") MultipartFile file) {
        try {
            Image image = this.imageService.uploadImageToFolder(file);
//            String uploadImage = String.valueOf(imageService.uploadImageToFolder(file));

            UploadImageResult uploadImageResult = new UploadImageResult();
            uploadImageResult.setName(image.getName());
            uploadImageResult.setImageUrl(image.getFilePath());
            uploadImageResult.setType(image.getType());

            return ResponseEntity.status(HttpStatus.CREATED).body(uploadImageResult);
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
