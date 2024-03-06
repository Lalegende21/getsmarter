package com.getsmarter.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@RequestMapping(path = "/facture")
public class Facture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name_student", nullable = false)
    private String nameStudent;

    private String matricule;

    private String formation;

    private String typeFormation;

    private String centre;

    private String typePaiement;

    private String dureeFormation;

    private BigDecimal montantPaye;

    private LocalDateTime datePaiement;

    private BigDecimal montantTotal;

    private BigDecimal montantRestant;

    private BigDecimal montantPayable;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "paiement_id", referencedColumnName = "id")
    private Paiement paiements;

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
