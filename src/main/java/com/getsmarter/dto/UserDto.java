package com.getsmarter.dto;

import com.getsmarter.entities.Role;
import com.getsmarter.enums.Sexe;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDto {

    private Long id;

    private String firstname;

    private String lastname;

    private String email;

    private String phonenumber;

    private Sexe sexe;

    private String country;

    private String city;

    private String dob;

    private String image;

    private Role role;

    private LocalDateTime created_at;
}
