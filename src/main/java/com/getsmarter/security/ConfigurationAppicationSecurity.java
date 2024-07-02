package com.getsmarter.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class ConfigurationAppicationSecurity {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtFilter jwtFilter;

    public ConfigurationAppicationSecurity(BCryptPasswordEncoder bCryptPasswordEncoder, JwtFilter jwtFilter) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        authorize ->
                                authorize
                                        .requestMatchers("/image/**").permitAll()

                                        //user
                                        .requestMatchers(HttpMethod.POST,
                                                "/user/save-user",
                                                "/user/activation-user",
                                                "/user/refresh-token",
                                                "/user/connexion-user",
                                                "/user/reset-password",
                                                "/user/update-password",
                                                "/user/deconnexion-user",
                                                "/user/save-image/**").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/user/change-role-admin-to-user/**").hasRole("ADMIN")
                                        .requestMatchers(HttpMethod.POST, "/user/change-role-user-to-admin/**").hasRole("ADMIN")
                                        .requestMatchers(HttpMethod.GET, "/user/profil").hasAnyRole("USER", "ADMIN")
                                        .requestMatchers(HttpMethod.GET,
                                                "/user/get-all-users",
                                                "/user/get-users-frequently",
                                                "/user/get-user/**").hasRole("ADMIN")
                                        .requestMatchers(HttpMethod.PUT, "/user/update-user/**").hasAnyRole("USER", "ADMIN")
                                        .requestMatchers(HttpMethod.DELETE, "/user/delete-all-users").hasRole("ADMIN")
                                        .requestMatchers(HttpMethod.DELETE, "/user/delete-user/**").hasAnyRole("USER", "ADMIN")


                                        //Center
                                        .requestMatchers(HttpMethod.GET, "/center/**").hasAnyRole("USER", "ADMIN")
                                        .requestMatchers(HttpMethod.POST, "center/**").hasRole("ADMIN")
                                        .requestMatchers(HttpMethod.PUT, "center/**").hasRole("ADMIN")
                                        .requestMatchers(HttpMethod.DELETE, "center/**").hasRole("ADMIN")

                                        //Course
                                        .requestMatchers(HttpMethod.GET, "/course/**").hasAnyRole("USER", "ADMIN")
                                        .requestMatchers(HttpMethod.POST, "course/**").hasRole("ADMIN")
                                        .requestMatchers(HttpMethod.PUT, "course/**").hasRole("ADMIN")
                                        .requestMatchers(HttpMethod.DELETE, "course/**").hasRole("ADMIN")

                                        //formation
                                        .requestMatchers(HttpMethod.GET, "/formation/**").hasAnyRole("USER", "ADMIN")
                                        .requestMatchers(HttpMethod.POST, "formation/**").hasRole("ADMIN")
                                        .requestMatchers(HttpMethod.PUT, "formation/**").hasRole("ADMIN")
                                        .requestMatchers(HttpMethod.DELETE, "formation/**").hasRole("ADMIN")

                                        //paiement
                                        .requestMatchers(HttpMethod.GET, "/paiement/**").hasAnyRole("USER", "ADMIN")
                                        .requestMatchers(HttpMethod.POST, "paiement/**").hasRole("ADMIN")
                                        .requestMatchers(HttpMethod.PUT, "paiement/**").hasRole("ADMIN")
                                        .requestMatchers(HttpMethod.DELETE, "paiement/**").hasRole("ADMIN")

                                        //professor
                                        .requestMatchers(HttpMethod.GET, "/professor/**").hasAnyRole("USER", "ADMIN")
                                        .requestMatchers(HttpMethod.POST, "professor/**").hasRole("ADMIN")
                                        .requestMatchers(HttpMethod.PUT, "professor/**").hasRole("ADMIN")
                                        .requestMatchers(HttpMethod.DELETE, "professor/**").hasRole("ADMIN")

                                        //session
                                        .requestMatchers(HttpMethod.GET, "/session/**").hasAnyRole("USER", "ADMIN")
                                        .requestMatchers(HttpMethod.POST, "session/**").hasRole("ADMIN")
                                        .requestMatchers(HttpMethod.PUT, "session/**").hasRole("ADMIN")
                                        .requestMatchers(HttpMethod.DELETE, "session/**").hasRole("ADMIN")

                                        //specificite-formation
                                        .requestMatchers(HttpMethod.GET, "/specificite-formation/**").hasAnyRole("USER", "ADMIN")
                                        .requestMatchers(HttpMethod.POST, "specificite-formation/**").hasRole("ADMIN")
                                        .requestMatchers(HttpMethod.PUT, "specificite-formation/**").hasRole("ADMIN")
                                        .requestMatchers(HttpMethod.DELETE, "specificite-formation/**").hasRole("ADMIN")

                                        //startCourse
                                        .requestMatchers(HttpMethod.GET, "/startCourse/**").hasAnyRole("USER", "ADMIN")
                                        .requestMatchers(HttpMethod.DELETE, "startCourse/**").hasRole("ADMIN")

                                        //student
                                        .requestMatchers(HttpMethod.GET, "/student/**").hasAnyRole("USER", "ADMIN")
                                        .requestMatchers(HttpMethod.POST, "/student/**").hasRole("ADMIN")
                                        .requestMatchers(HttpMethod.PUT, "/student/**").hasRole("ADMIN")
                                        .requestMatchers(HttpMethod.DELETE, "/student/**").hasRole("ADMIN")

                                        //tutor
                                        .requestMatchers(HttpMethod.GET, "/tutor/**").hasAnyRole("USER", "ADMIN")
                                        .requestMatchers(HttpMethod.POST, "tutor/**").hasRole("ADMIN")
                                        .requestMatchers(HttpMethod.PUT, "tutor/**").hasRole("ADMIN")
                                        .requestMatchers(HttpMethod.DELETE, "tutor/**").hasRole("ADMIN")


                                        .anyRequest().authenticated()
                )
                .sessionManagement(httpSecuritySessionManagementConfigurer ->
                        httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);
        return daoAuthenticationProvider;
    }
}
