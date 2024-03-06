package com.getsmarter.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "Validation")
public class Validation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Instant dateCreation;

    private Instant expire;

    private Instant activation;

    private String code;

    @OneToOne(cascade = CascadeType.ALL)
    private Admin admin;

    @JsonIgnore
    @Column(name = "created_at", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd/HH-mm-ss")
    private LocalDateTime created_at;

    @JsonIgnore
    @Column(name = "update_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd/HH-mm-ss")
    @UpdateTimestamp
    private Timestamp updated_at;
}
