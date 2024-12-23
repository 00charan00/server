package com.example.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private static final String[] staffAdminAuthEndPoints = {
            "/staff/del",
            "/staff/all",
            "/staff/update",
            "/team/**"
    };

    private static final String[] teamAdminAuthEndPoints = {
            "/team/",
    };

    private static final String[] taskAdminAuthEndPoints = {
            "/task/create",
            "/task/all",
            "/task/get/",
            "/task/get/",
            "/task/in-review",
            "/task/approve",
            "/task/assign",
    };

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(
                        req -> {
                            req.requestMatchers(staffAdminAuthEndPoints).hasRole("ADMIN");
                            req.requestMatchers(teamAdminAuthEndPoints).hasRole("ADMIN");
                            req.requestMatchers(taskAdminAuthEndPoints).hasRole("ADMIN");
                            req.requestMatchers("/staff/add","/staff/login","/staff/home").permitAll();
                            req.anyRequest().authenticated();
                        }
                )
                .build();
    }


    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
