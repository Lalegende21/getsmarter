package com.getsmarter.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "formation")
public class Formation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;


    @OneToOne
    @JoinColumn(name = "code_formation_id", referencedColumnName = "id")
    private CodeFormation codeFormation;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "duree_id", referencedColumnName = "id")
    private DureeFormation duree;

    @ManyToOne
    @JoinColumn(name = "type_formation_id", referencedColumnName = "id")
    private TypeFormation typeFormation;

    @JsonIgnore
    @OneToOne(mappedBy = "formation", cascade = CascadeType.ALL)
    private Student student;

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
