package com.sjfjuristas.plataforma.backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(auth -> auth

            // Endpoints públicos
            .requestMatchers("/api/auth/**", "/api/propostas/**", "/api/ocupacoes/**", "/api/cliente/**", "/api/admin/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/admins/password/request-reset", "/api/admins/password/reset-with-token").permitAll()
            
            // Endpoints de Administrador (exemplo, ajuste os perfis se necessário)
            //.requestMatchers("/api/admin/**").hasAuthority("Administrador")
            // Endpoints de Cliente
            //.requestMatchers("/api/cliente/**").hasAuthority("Cliente")
            // Qualquer outra requisição precisa estar autenticada
            .requestMatchers("/swagger-ui.html","/swagger-ui/**","/v3/api-docs/**","/v3/api-docs").permitAll()
            
            .anyRequest().authenticated())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}