package com.getsmarter.repositories;

import com.getsmarter.entities.Jwt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.stream.Stream;

public interface JwtRepo extends JpaRepository<Jwt, Long> {
    Optional<Jwt> findByValeur(String valeur);

    Optional<Jwt> findByValeurAndDesactiveAndExpire(String valeur, boolean desactive, boolean expire);

    @Query("FROM Jwt j WHERE j.expire = :expire AND j.desactive = :desactive AND j.user.email = :email")
    Optional<Jwt> findUserValidToken(String email, boolean desactive, boolean expire);

    @Query("FROM Jwt j WHERE j.user.email = :email")
    Stream<Jwt> findUser(String email);

    @Query("FROM Jwt j WHERE j.refreshToken.valeur = :valeur")
    Optional<Jwt> findByRefreshToken(String valeur);


    void deleteAllByExpireAndDesactive(boolean expire, boolean desactive);

    void deleteByUserId(Long id);
}
