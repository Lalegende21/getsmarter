package com.getsmarter.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "type_tutor")
public class TypeTutor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type", nullable = false)
    private String type;

    @JsonIgnore
    @OneToOne(mappedBy = "type", cascade = CascadeType.ALL)
    private Tutor tutor;

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
