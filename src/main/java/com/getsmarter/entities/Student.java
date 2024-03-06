package com.getsmarter.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.getsmarter.enums.Sexe;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "student")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "firstname", nullable = false)
    private String firstname;

    @Column(name = "lastname", nullable = false)
    private String lastname;

    @Column(name = "dob", nullable = false)
    private String dob;

    @Column(name = "matricule", nullable = false, unique = true)
    private String matricule;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phonenumber", nullable = false)
    private String phonenumber;

    @Column(name = "cni", nullable = false)
    private String cni;

    @Enumerated(EnumType.STRING)       //Permet de stocker le sexe sous forme de chaine de caractere
    @Column(name = "sexe", nullable = false)
    private Sexe sexe;

    @Column(name = "montant_paye")
    private BigDecimal montantPaye;

    @Column(name = "montant_restant_a_payer")
    private BigDecimal montantRestantaPayer;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "student")
    private List<Facture> factures;

//    @OneToOne
//    @JoinColumn(name = "image_id", referencedColumnName = "id")
//    private Image image;

    @OneToOne
    @JoinColumn(name = "admin_id", referencedColumnName = "id")
    private Admin admin;


    @OneToOne
    @JoinColumn(name = "center_id", referencedColumnName = "id")
    private Center center;

    @JsonIgnore
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Paiement> paiement;

    @OneToOne
    @JoinColumn(name = "tutor_id", referencedColumnName = "id")
    private Tutor tutor;

    @OneToOne
    @JoinColumn(name = "formation_id", referencedColumnName = "id")
    private Formation formation;

    @JsonIgnore
    @Column(name = "created_at", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd/HH-mm-ss")
    private LocalDateTime created_at;

    @JsonIgnore
    @Column(name = "update_at")
    @UpdateTimestamp
    @DateTimeFormat(pattern = "yyyy-MM-dd/HH-mm-ss")
    private Timestamp updated_at;

}
