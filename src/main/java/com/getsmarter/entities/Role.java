package com.getsmarter.entities;

import com.getsmarter.enums.TypeRole;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)       //Permet de stocker le role sous forme de chaine de caractere
    private TypeRole libelle;
}
