package com.getsmarter.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "formation")
public class Formation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "specialite", nullable = false, unique = true)
    private String specialite;

    @ManyToOne
    @JoinColumn(name = "formation_id", referencedColumnName = "id")
    private SpecificiteFormation specificiteFormation;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "periode", nullable = false)
    private String periode;

    private String image;

    @JsonIgnore
    @OneToMany(mappedBy = "formation", cascade = {CascadeType.DETACH, CascadeType.MERGE})
    private List<Student> student;

    @Column(name = "created_at", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd/HH-mm-ss")
    private LocalDateTime created_at;

    @PrePersist
    public void prePersist() {
        this.created_at = LocalDateTime.now();
    }

    @JsonIgnore
    @Column(name = "update_at")
    @UpdateTimestamp
    @DateTimeFormat(pattern = "yyyy-MM-dd/HH-mm-ss")
    private Date updated_at;
}
