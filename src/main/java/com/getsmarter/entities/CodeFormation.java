package com.getsmarter.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@RequestMapping(path = "/code-formation")
public class CodeFormation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false)
    private String code;

    @JsonIgnore
    @OneToOne(mappedBy = "codeFormation", cascade = CascadeType.ALL)
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
