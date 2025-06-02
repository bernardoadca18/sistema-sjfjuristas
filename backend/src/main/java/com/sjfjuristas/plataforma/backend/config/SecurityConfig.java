package com.sjfjuristas.plataforma.backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    // private final JwtAuthFilter jwtAuthFilter; // Criaremos a seguir
    // private final AuthenticationProvider authenticationProvider; // Criaremos a seguir

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF (Cross-Site Request Forgery)
                .csrf(csrf -> csrf.disable())
                // Regras de autorização para os endpoints
                .authorizeHttpRequests(auth -> auth
                        // Endpoints públicos de autenticação
                        .requestMatchers("/api/auth/**").permitAll() 
                        // Qualquer outra requisição deve ser autenticada
                        .anyRequest().authenticated() 
                )

                // Gerenciamento de sessão como STATELESS
                // O servidor não criará ou manterá sessões HTTP. Cada requisição é independente.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

                // Provedor de autenticação personalizado
                // .authenticationProvider(authenticationProvider)

                // Filtro JWT antes do filtro padrão de autenticação
                // .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}
