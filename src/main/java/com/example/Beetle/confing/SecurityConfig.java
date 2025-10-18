package com.example.Beetle.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Desactiva la protección CSRF (para pruebas locales con Postman);
                .authorizeHttpRequests()
                .anyRequest().permitAll(); // Permite todas las rutas;

        return http.build();
    }
}
