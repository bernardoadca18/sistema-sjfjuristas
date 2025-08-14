package com.sjfjuristas.plataforma.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    //private final JwtAuthFilter jwtAuthFilter;
    //private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(auth -> auth
                // ENDPOINTS PUBLICOS    
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/admin/auth/login").permitAll()
                .requestMatchers("/api/senha/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/propostas").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/ocupacoes").permitAll()
                .requestMatchers("/api/tipo-chave-pix/**").permitAll()
                
                // ENDPOINTS DE DOCUMENTAÇÃO SWAGGER
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").hasRole("ADMINISTRADOR")

                // ENDPOINTS DE CLIENTES
                .requestMatchers("/api/cliente/**").authenticated()
                .requestMatchers("/api/emprestimos/**").authenticated()

                // ENDPOINTS DE ADMINISTRADOR
                .requestMatchers("/api/admin/**").hasRole("ADMINISTRADOR")
                .requestMatchers("/api/admins/**").hasRole("ADMINISTRADOR")

                .anyRequest().authenticated()
            
            );
        
        return http.build();
    }
}