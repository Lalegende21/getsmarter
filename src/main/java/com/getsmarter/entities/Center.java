package com.getsmarter.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "center")
public class Center {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotEmpty
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "localisation", nullable = false)
    private String localisation;

    private String image;

    @JsonIgnore
    @OneToMany(mappedBy = "center", cascade = {CascadeType.DETACH, CascadeType.MERGE})
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
    @DateTimeFormat(pattern = "yyyy-MM-dd/HH-mm-ss")
    @UpdateTimestamp
    private Date updated_at;
}
