package com.getsmarter.controllers;

import com.getsmarter.entities.CodeFormation;
import com.getsmarter.services.CodeFormationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/code-formation")
public class CodeFormaterController {

    private final CodeFormationService codeFormationService;


    @PostMapping(path = "/save-code-formation")
    public ResponseEntity<String> saveCodeFormation(@RequestBody CodeFormation codeFormation) {
        try {
            this.codeFormationService.saveCode(codeFormation);
            return new ResponseEntity<>("Code formation register successfully !", HttpStatus.CREATED);
        }catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Something went wrong: "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping(path = "/get-all-code-formation")
    public List<CodeFormation> getAllCodeFormation() {
        return this.codeFormationService.getAllCode();
    }



    @GetMapping(path = "/get-code-formation/{id}")
    public CodeFormation getCodeFormationById(@PathVariable Long id) {
        try {
            return this.codeFormationService.getCodeById(id);
        }catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }



    @PutMapping(path = "/update-code-formation/{id}")
    public ResponseEntity<String> updateCodeFormation(@PathVariable Long id, @RequestBody CodeFormation codeFormation) {
        try {
            this.codeFormationService.updateCode(id, codeFormation);
            return new ResponseEntity<>("Code formation update successfully !", HttpStatus.ACCEPTED);
        }catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Something went wrong: "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @DeleteMapping(path = "/delete-all-code-formation")
    public ResponseEntity<String> deleteAllCodeFormation() {
        try {
            this.codeFormationService.deleteAllCode();
            return new ResponseEntity<>("All Code formation delete successfully !", HttpStatus.OK);
        }catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Something went wrong: "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @DeleteMapping(path = "/delete-code-formation/{id}")
    public ResponseEntity<String> deleteCodeFormationById(@PathVariable Long id) {
        try {
            this.codeFormationService.deleteCodeById(id);
            return new ResponseEntity<>("Code formation with id: " +id+ " delete successfully !", HttpStatus.OK);
        }catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Something went wrong: "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
