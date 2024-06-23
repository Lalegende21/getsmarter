package com.getsmarter.security;

import com.getsmarter.entities.Jwt;
import com.getsmarter.entities.RefreshToken;
import com.getsmarter.entities.User;
import com.getsmarter.repositories.JwtRepo;
import com.getsmarter.services.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
@AllArgsConstructor
public class JwtService {

    public static final String BEARER = "bearer";
    public static final String REFRESH = "refresh";
    public static final String TOKEN_INVALIDE = "Token invalide";
    private final String ENCRYPTION_KEY = "ce28755d0f1245c6a0f9ebc6da62fe54509fe9fbe27b732468829abba979c88f";

    private UserService userService;

    private JwtRepo jwtRepo;


    public Jwt tokenByValue(String valeur) {
        return this.jwtRepo.findByValeurAndDesactiveAndExpire(
                valeur,
                false,
                false
        ).orElseThrow(() -> new EntityNotFoundException("Aucun token trouve!"));
    }

    public Map<String, String> generate(String username) {
        User user = (User) this.userService.loadUserByUsername(username);
        User userEmail = this.userService.getByEmail(username);
        String role = String.valueOf(userEmail.getRole().getLibelle());
        boolean status = userEmail.getStatus();

        this.disableTokens(user);
        Map<String, String> jwtMap = new HashMap<>( this.generateJwt(user));
        RefreshToken refreshToken = RefreshToken.builder()
                .valeur(UUID.randomUUID().toString())
                .expire(false)
                .expiration(Instant.now().plusMillis(60 *60 *1000))
                .creation(Instant.now().plusMillis(30 *60 *1000))
                .created_at(LocalDateTime.now())
                .build();

        Jwt jwt = Jwt
                .builder()
                .valeur(jwtMap.get(BEARER))
                .desactive(false)
                .expire(false)
                .user(user)
                .refreshToken(refreshToken)
                .created_at(LocalDateTime.now())
                .build();
        this.jwtRepo.save(jwt);
        jwtMap.put("Role", role);
        jwtMap.put("status", String.valueOf(status));
        jwtMap.put(REFRESH, refreshToken.getValeur());
        return jwtMap;
    }

    private void disableTokens(User user) {
        final List<Jwt> jwtList = this.jwtRepo.findUser(user.getEmail()).peek(
                jwt -> {
                    jwt.setDesactive(true);
                    jwt.setExpire(true);
                }
        ).collect(Collectors.toList());
        this.jwtRepo.saveAll(jwtList);
    }

    public String extractUsername(String token) {
        return this.getClaim(token, Claims::getSubject);
    }

    public Boolean isTokenExpired(String token) {
        Date expirationDate = this.getClaim(token, Claims::getExpiration);
        return expirationDate.before(new Date());
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

    private Map<String, String> generateJwt(User user) {

        final long currentTime = System.currentTimeMillis();
        final long expirationTime = currentTime + 24 * 7 * 4 * 60 * 1000;

        //Les claims sont les informations de l'utilisateur qu'on veut retrouver dans le jwt
        Map<String, Object> claims = Map.of(
                "Nom", user.getFirstname(),
                "Prenom", user.getLastname(),
                    "role", user.getRole().getLibelle(),
//                "Numero de telephone", user.getPhonenumber(),
//                "Sexe", user.getSexe().name(),
                Claims.EXPIRATION, new Date(expirationTime),
                Claims.SUBJECT, user.getEmail()
        );


        String bearer = Jwts.builder()
                .setIssuedAt(new Date(currentTime))
                .setExpiration(new Date(expirationTime))
                .setSubject(user.getEmail())
                .setClaims(claims)
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
        return Map.of(BEARER, bearer);
    }

    private Key getKey() {
        byte[] decoder = Decoders.BASE64.decode(ENCRYPTION_KEY);
        return Keys.hmacShaKeyFor(decoder);
    }

    public void deconnexion() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Jwt jwt = this.jwtRepo.findUserValidToken(
                user.getEmail(),
                false,
                false
        ).orElseThrow(() -> new RuntimeException(TOKEN_INVALIDE));
        jwt.setExpire(true);
        jwt.setDesactive(true);
        this.jwtRepo.save(jwt);
    }


    @Scheduled(cron = "0 */1 * * * *")
    public void removeUselessJwt() {
        log.info("Suppression des tokens a {}", Instant.now());
        this.jwtRepo.deleteAllByExpireAndDesactive(true, true);
    }

    public Map<String, String> refreshToken(Map<String, String> refreshTokenRequest) {
        Jwt jwt = this.jwtRepo.findByRefreshToken(refreshTokenRequest.get(REFRESH)).orElseThrow(() -> new RuntimeException(TOKEN_INVALIDE));
        if (jwt.getRefreshToken().isExpire() || jwt.getRefreshToken().getExpiration().isBefore(Instant.now())) {
            throw new RuntimeException(TOKEN_INVALIDE);
        }
        Map<String, String> tokens = this.generate(jwt.getUser().getEmail());
        this.disableTokens(jwt.getUser());

        jwt.setValeur(tokens.get("bearer"));
        this.jwtRepo.save(jwt);
        return tokens;
    }
}
