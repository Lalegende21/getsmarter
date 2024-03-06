package com.getsmarter.services;

import com.getsmarter.entities.Image;
import com.getsmarter.repositories.ImageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class ImageService {

    @Autowired
    private ImageRepo imageRepo;


    private String FOLDER_PATH = "D:\\Mes programmes\\Spring Boot\\getsmarter\\src\\main\\java\\com\\getsmarter\\folder\\";



    //Methode pour enregistrer une image
    public Image uploadImageToFolder(MultipartFile file) throws IOException {
        if (file.getSize() > 3 * 1024 * 1024) {
            throw new IllegalArgumentException("Image size exceeds the limit of 3MB.");
        }

        String filePath = FOLDER_PATH + file.getOriginalFilename();

        Image imageBuilder = Image.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .filePath(filePath).build();

        Optional<Image> optionalImage = this.imageRepo.findByName(imageBuilder.getName());
        if (optionalImage.isPresent()) {
            throw new RuntimeException("Cette image existe deja !");
        }

        imageBuilder.setCreated_at(LocalDateTime.now());
        Image fileData = imageRepo.save(imageBuilder);

        file.transferTo(new File(filePath));

        if (filePath != null){
            System.out.println("File uploaded successfully: " + filePath);
        }
        return imageBuilder;
    }



    //Methode pour lire une image dans la base de donnee
    public byte[] donwloadImageFomFolder(String fileName) throws IOException {
        Optional<Image> fileData = imageRepo.findByName(fileName);
        String filePath = fileData.get().getFilePath();
        byte[] image = Files.readAllBytes(new File(filePath).toPath());
        return image;
    }



    //Methode pour faire la mise a jour de l'image
    public String updateImageInFolder(MultipartFile file, Long fileId) throws IOException {
        // Récupérer le fichier existant à mettre à jour
        Optional<Image> optionalFileData = imageRepo.findById(fileId);
        if (optionalFileData.isPresent()) {
            Image fileData = optionalFileData.get();

            // Supprimer l'ancien fichier du dossier
            File oldFile = new File(fileData.getFilePath());
            oldFile.delete();

            // Enregistrer le nouveau fichier
            String filePath = FOLDER_PATH + file.getOriginalFilename();
            file.transferTo(new File(filePath));

            // Mettre à jour les informations du fichier dans la base de données
            fileData.setName(file.getOriginalFilename());
            fileData.setType(file.getContentType());
            fileData.setFilePath(filePath);
            imageRepo.save(fileData);

            return "File updated successfully: " + filePath;
        }

        // Le fichier avec l'ID spécifié n'existe pas
        return "File not found with ID: " + fileId;
    }





    //Methode pour supprimer toutes les images dans le dossier et dans la base de donnee
    public void deleteAllImages() {
        // Supprimer les images dans le dossier
        deleteAllImagesInFolder();

        // Supprimer les images dans la base de données
        imageRepo.deleteAll();
    }

    //Methode pour supprimer toutes les images dans le dossier
    private void deleteAllImagesInFolder() {
        File folder = new File(FOLDER_PATH);
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        file.delete();
                    }
                }
            }
        }
    }





    //Methode pour supprimer l'image par son id dans le dossier et la base de donnee
    public void deleteImageById(Long fileId) {
        // Supprimer l'image dans le dossier
        deleteImageInFolderById(fileId);

        // Supprimer l'image dans la base de données
        imageRepo.deleteById(fileId);
    }

    //Methode pour supprimer une image par son id dans le dossier
    private void deleteImageInFolderById(Long fileId) {
        Optional<Image> optionalFileData = imageRepo.findById(fileId);
        if (optionalFileData.isPresent()) {
            Image fileData = optionalFileData.get();
            String filePath = fileData.getFilePath();
            File file = new File(filePath);
            file.delete();
        }
    }
}
