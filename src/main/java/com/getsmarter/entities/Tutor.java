package com.getsmarter.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.getsmarter.enums.TypeTutor;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "tutor")
public class Tutor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(name = "fullname", nullable = false)
    private String fullname;

    @Column(name = "email")
    private String email;

    @NotEmpty
    @Column(name = "phonenumber", nullable = false, unique = true)
    private String phonenumber;

    @Column(name = "type_tutor", nullable = false)
    @Enumerated(EnumType.STRING)
    private TypeTutor typeTutor;

    @OneToMany(cascade = {CascadeType.DETACH}, orphanRemoval = true)
    @JoinColumn(name = "tutor_id")
    private List<Student> students = new ArrayList<>();

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

//    public List<Student> getStudents() {
//        return this.students;
//    }
}
