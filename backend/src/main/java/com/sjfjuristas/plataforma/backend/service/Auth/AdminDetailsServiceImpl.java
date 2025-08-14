package com.sjfjuristas.plataforma.backend.service.Auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sjfjuristas.plataforma.backend.repository.AdministradorRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminDetailsServiceImpl implements UserDetailsService {

    private final AdministradorRepository administradorRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return administradorRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Administrador não encontrado: " + username));
    }
}