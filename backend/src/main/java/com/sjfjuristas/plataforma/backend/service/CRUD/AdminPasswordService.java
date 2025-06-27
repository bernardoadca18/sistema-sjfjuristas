package com.sjfjuristas.plataforma.backend.service.CRUD;

import com.sjfjuristas.plataforma.backend.domain.Administrador;
import com.sjfjuristas.plataforma.backend.dto.Admin.AdminUpdatePasswordRequestDTO;
import com.sjfjuristas.plataforma.backend.dto.Admin.AdminUpdatePasswordWithOldPasswordDTO;
import com.sjfjuristas.plataforma.backend.dto.Admin.AdminUpdatePasswordWithTokenDTO;
import com.sjfjuristas.plataforma.backend.repository.AdministradorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;
import java.util.UUID;

@Service
public class AdminPasswordService
{
    @Autowired
    private AdministradorRepository administradorRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public void requestPasswordReset(AdminUpdatePasswordRequestDTO request)
    {
        Administrador administrador = administradorRepository.findByEmail(request.getEmail()).orElseThrow(() -> new EntityNotFoundException("Administrador não encontrado com o e-mail: " + request.getEmail()));

        String token = UUID.randomUUID().toString();
        administrador.setTokenRecuperacaoSenha(token);
        administrador.setValidadeTokenRecuperacao(OffsetDateTime.now().plusHours(1));
        
        administradorRepository.save(administrador);

        // TODO: Implementar o envio de e-mail com o token para o administrador
        System.out.println("Token de recuperação para " + administrador.getEmail() + ": " + token);
    }

    @Transactional
    public void resetPasswordWithToken(AdminUpdatePasswordWithTokenDTO request)
    {
        if (!request.getNovaSenha().equals(request.getConfirmarNovaSenha()))
        {
            throw new IllegalArgumentException("As senhas não coincidem.");
        }

        Administrador administrador = administradorRepository.findByTokenRecuperacaoSenha(request.getToken()).orElseThrow(() -> new EntityNotFoundException("Token de recuperação inválido ou expirado."));

        if (administrador.getValidadeTokenRecuperacao().isBefore(OffsetDateTime.now()))
        {
            throw new IllegalArgumentException("Token de recuperação expirado.");
        }

        administrador.setHashSenha(passwordEncoder.encode(request.getNovaSenha()));
        administrador.setTokenRecuperacaoSenha(null);
        administrador.setValidadeTokenRecuperacao(null);

        administradorRepository.save(administrador);
    }

    @Transactional
    public void updatePassword(AdminUpdatePasswordWithOldPasswordDTO request)
    {
        if (!request.getNovaSenha().equals(request.getConfirmarNovaSenha()))
        {
            throw new IllegalArgumentException("As senhas não coincidem.");
        }

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();

        Administrador administrador = administradorRepository.findByEmail(username).orElseThrow(() -> new EntityNotFoundException("Administrador não encontrado com o e-mail: " + username));

        if (!passwordEncoder.matches(request.getSenhaAntiga(), administrador.getHashSenha()))
        {
            throw new IllegalArgumentException("A senha atual está incorreta.");
        }

        administrador.setHashSenha(passwordEncoder.encode(request.getNovaSenha()));
        administradorRepository.save(administrador);
    }

    
}
