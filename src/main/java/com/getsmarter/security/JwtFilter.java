package com.getsmarter.security;

import com.getsmarter.entities.Jwt;
import com.getsmarter.entities.User;
import com.getsmarter.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Service
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    UserService userService;
    JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token;
        Jwt tokenDansLaBDD = null;
        String username = null;
        Boolean isTokenExpired = true;


        // Bearer eyJhbGciOiJIUzI1NiJ9.eyJFbWFpbCI6InRlbmVkZWxmcmVkMTlAZ21haWwuY29tIiwiTm9tIjoiVGVuZSIsIk51bWVybyBkZSB0ZWxlcGhvbmUiOiI2Nzg5OTgwMjAiLCJQcmVub20iOiJEZWxmcmVkIn0.SB_T3S8XV_bitjAZ2P5gq1JEd78gYtYA76tA_MVwwe0
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer ")) {
            token = authorization.substring(7);
            tokenDansLaBDD = this.jwtService.tokenByValue(token);
            isTokenExpired = jwtService.isTokenExpired(token);
            username = jwtService.extractUsername(token);
        }

        if (
                !isTokenExpired
                && tokenDansLaBDD.getUser().getEmail().equals(username)
                && SecurityContextHolder.getContext().getAuthentication() == null
        ) {
            UserDetails userDetails = userService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        filterChain.doFilter(request, response);

    }
}
