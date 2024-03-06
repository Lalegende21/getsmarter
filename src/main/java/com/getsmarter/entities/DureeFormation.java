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
import java.util.List;

@Entity
@Data
@Table(name = "duree_formation")
public class DureeFormation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "periode", nullable = false)
    private String periode;

    @JsonIgnore
    @OneToMany(mappedBy = "duree", cascade = CascadeType.ALL)
    private List<Formation> formation;

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
