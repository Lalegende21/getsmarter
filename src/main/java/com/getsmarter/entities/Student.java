package com.getsmarter.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.getsmarter.enums.Horaire;
import com.getsmarter.enums.Sexe;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
    @DateTimeFormat(pattern = "yyyy-MM-dd/HH-mm-ss")
    private String dob;

    @Column(name = "matricule", nullable = false, unique = true)
    private String matricule;

    @Column(name = "email")
    private String email;

    @Column(name = "phonenumber", nullable = false)
    private String phonenumber;

    @Column(name = "cni")
    private String cni;

    @Column(name = "horaire", nullable = false)
    private String horaire;

    @Column(name = "image")
    private String image;

    @Enumerated(EnumType.STRING)       //Permet de stocker le sexe sous forme de chaine de caractere
    @Column(name = "sexe", nullable = false)
    private Sexe sexe;

    @Column(name = "montant_Total")
    private BigDecimal montantTotal;

    @Column(name = "montant_paye")
    private BigDecimal montantPaye;

    @Column(name = "montant_restant_a_payer")
    private BigDecimal montantRestantaPayer;


    @ManyToOne
    @JoinColumn(name = "session_id", referencedColumnName = "id")
    private Session session;


    @ManyToOne
    @JoinColumn(name = "admin_id", referencedColumnName = "id", nullable = true)
    private User user;


    @ManyToOne
    @JoinColumn(name = "center_id", referencedColumnName = "id")
    private Center center;

    @JsonIgnore
    @OneToMany(mappedBy = "student", cascade = {CascadeType.MERGE, CascadeType.DETACH})
    private List<Paiement> paiement;

    @ManyToOne
    @JoinColumn(name = "formation_id", referencedColumnName = "id")
    private Formation formation;

    @Column(name = "created_at", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd/HH-mm-ss")
    private LocalDateTime created_at;

    @PrePersist
    public void prePersist() {
        this.created_at = LocalDateTime.now();
    }

    @JsonIgnore
    @Column(name = "updated_at")
    @UpdateTimestamp
    @DateTimeFormat(pattern = "yyyy-MM-dd/HH-mm-ss")
    private Date updated_at;

    private String fullName() {
        return firstname + " " + lastname;
    }

}
