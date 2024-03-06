package com.getsmarter.repositories;

import com.getsmarter.entities.Jwt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.stream.Stream;

public interface JwtRepo extends JpaRepository<Jwt, Long> {
    Optional<Jwt> findByValueAndDisableAndExpired(String value, boolean disable, boolean expired);

    @Query("FROM Jwt j WHERE j.expired = :expired AND j.disable = :disable AND j.admin.email = :email")
    Optional<Jwt> findAdminValidToken(String email, boolean disable, boolean expired);

    @Query("FROM Jwt j WHERE j.admin.email = :email")
    Stream<Jwt> findAdminByEmail(String email);

    @Query("FROM Jwt j WHERE j.admin.email = :email")
    Stream<Jwt> findAdmin(String email);

    void deleteAllByExpiredAndDisable(boolean expired, boolean disable);
}
