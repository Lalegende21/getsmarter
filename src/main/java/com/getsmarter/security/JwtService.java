package com.getsmarter.security;

import com.getsmarter.dto.AuthentificationDto;
import com.getsmarter.entities.Admin;
import com.getsmarter.entities.Jwt;
import com.getsmarter.repositories.JwtRepo;
import com.getsmarter.services.AdminService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Transactional
@Slf4j
@AllArgsConstructor
@Service
public class JwtService {


    public static final String BEARER = "bearer";
    private final String ENCRIPTION_KEY = "608f36e92dc66d97d5933f0e6371493cb4fc05b1aa8f8de64014732472303a7c";
    private AdminService adminService;

    private JwtRepo jwtRepo;


    public Jwt tokenByValue(String value) {
        return this.jwtRepo.findByValueAndDisableAndExpired(
                value,
                false,
                false).orElseThrow(() -> new RuntimeException("Token not found !"));
    }

    public Map<String, String> generate(String username) {
        Admin admin = (Admin) this.adminService.loadUserByUsername(username);
        this.disableTokens(admin);

        //On recupere le bearer
        Map<String, String> jwtMap = this.generateJwt(admin);

        Jwt jwt = Jwt
                .builder()
                .value(jwtMap.get(BEARER))
                .disable(false)
                .expired(false)
                .created_at(LocalDateTime.now())
                .admin(admin)
                .build();
        //On le stocke dans la base de donnee
        this.jwtRepo.save(jwt);
        return jwtMap;
    }

    private void disableTokens(Admin admin) {
        final List<Jwt> jwtList = this.jwtRepo.findAdmin(admin.getEmail()).peek(
                jwt -> {
                    jwt.setDisable(true);
                    jwt.setExpired(true);
                }
        ).collect(Collectors.toList());
        this.jwtRepo.saveAll(jwtList);
    }

    public String extractUsername(String token) {
        return this.getClaim(token, Claims::getSubject);
    }

    public boolean isTokenExpired(String token) {
        Date expirationDate = getExpirationDateFromToken(token);
        return expirationDate.before(new Date());
    }

    private Date getExpirationDateFromToken(String token) {
        return this.getClaim(token, Claims::getExpiration);
    }

    private <T> T getClaim(String token, Function<Claims, T> function) {
        Claims claims = getAllClaims(token);
        return function.apply(claims);
    }

    private Claims getAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(this.getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Map<String, String> generateJwt(Admin admin) {
        final long currentTime = System.currentTimeMillis();
        final long expirationTime = currentTime + 30 * 60 * 1000;

        final Map<String, Object> claims = Map.of(
                "nom", admin.getFirstname()+admin.getLastname(),
                Claims.EXPIRATION, new Date(expirationTime),
                Claims.SUBJECT, admin.getEmail()
        );

        final String bearer = Jwts.builder()
                .setIssuedAt(new Date(currentTime))
                .setExpiration(new Date(expirationTime))
                .setSubject(admin.getEmail())
                .setClaims(claims)
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
        return Map.of(BEARER, bearer);
    }

    private Key getKey() {
        final byte[] decoder = Decoders.BASE64.decode(ENCRIPTION_KEY);
        return Keys.hmacShaKeyFor(decoder);
    }



    //Methode pour la deconnexion du compte
    public void deconnexion() {
        Admin admin = (Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Jwt jwt = this.jwtRepo.findAdminValidToken(
                admin.getEmail(),
                false,
                false).orElseThrow(() -> new RuntimeException("Invalid token !"));
        jwt.setExpired(true);
        jwt.setDisable(true);
        this.jwtRepo.save(jwt);
    }


    //Methode pour nettoyer la base de donnee
//    @Scheduled(cron = "@daily")
    @Scheduled(cron = "0 */5 * * * *")      //On nettoie la table jwt toutes les 5min dans la base de donnee
    public void removeUselessJwt() {
        log.info("Suppression des tokens à ¨{}", Instant.now());
        this.jwtRepo.deleteAllByExpiredAndDisable(true, true);
    }
}
