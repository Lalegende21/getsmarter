package com.getsmarter.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;

@RestController
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
public class TestController {


    @PostMapping(path = "/inscription")
    public String inscription() {
        return "La requete inscription est accessible !";
    }
}
