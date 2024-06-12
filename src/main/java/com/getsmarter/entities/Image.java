package com.getsmarter.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "image")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "filepath", nullable = false)
    private String filePath;

//    @JsonIgnore
//    @OneToOne(mappedBy = "image", cascade = CascadeType.ALL)
//    private User user;

    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE})
    private User user;

    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE})
    private Student student;

    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE})
    private Center center;

    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE})
    private Professor professor;

    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE})
    private Formation formation;

    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE})
    private Course course;

    @JsonIgnore
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
    private Timestamp updated_at;
}
