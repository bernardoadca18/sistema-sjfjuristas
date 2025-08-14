package com.sjfjuristas.plataforma.backend.service.Auth;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sjfjuristas.plataforma.backend.dto.Admin.AdminAuthResponseDTO;
import com.sjfjuristas.plataforma.backend.dto.Admin.AdminLoginRequestDTO;
import com.sjfjuristas.plataforma.backend.repository.AdministradorRepository;
import com.sjfjuristas.plataforma.backend.service.Jwt.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminAuthServiceImpl implements AdminAuthService
{
    private final AdministradorRepository administradorRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService adminDetailsServiceImpl;

    @SuppressWarnings("deprecation")
    @Override
    public AdminAuthResponseDTO login(AdminLoginRequestDTO request)
    {

        DaoAuthenticationProvider adminAuthProvider = new DaoAuthenticationProvider();
        adminAuthProvider.setUserDetailsService(adminDetailsServiceImpl);
        adminAuthProvider.setPasswordEncoder(passwordEncoder);

        
        adminAuthProvider.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getLogin(),
                request.getSenha()
            )
        );

        var admin = administradorRepository.findByUsername(request.getLogin())
                .orElseThrow(() -> new IllegalArgumentException("Administrador não encontrado."));

        String jwtToken = jwtService.generateToken(admin);

        return AdminAuthResponseDTO.builder()
                .token(jwtToken)
                .adminId(admin.getId())
                .nomeAdmin(admin.getNomeCompleto())
                .build();
    }
}