package com.sjfjuristas.plataforma.backend.service;

import com.sjfjuristas.plataforma.backend.domain.Usuario;
import com.sjfjuristas.plataforma.backend.dto.Usuario.ClienteResponseDTO;
import com.sjfjuristas.plataforma.backend.dto.Usuario.ClienteUpdateRequestDTO;
import com.sjfjuristas.plataforma.backend.dto.Usuario.PasswordUpdateRequestDTO;
import com.sjfjuristas.plataforma.backend.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UsuarioClienteService
{
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public ClienteResponseDTO getDadosCliente(UUID usuarioId)
    {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));
        return new ClienteResponseDTO(usuario);
    }

    @Transactional
    public ClienteResponseDTO updateDadosCliente(UUID usuarioId, ClienteUpdateRequestDTO requestDTO) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));

        if (requestDTO.getNomeCompleto() != null) {
            usuario.setNomeCompleto(requestDTO.getNomeCompleto());
        }
        if (requestDTO.getTelefoneWhatsapp() != null) {
            usuario.setTelefoneWhatsapp(requestDTO.getTelefoneWhatsapp());
        }
        if (requestDTO.getEnderecoCompleto() != null) {
            usuario.setEnderecoCompleto(requestDTO.getEnderecoCompleto());
        }

        Usuario usuarioAtualizado = usuarioRepository.save(usuario);
        return new ClienteResponseDTO(usuarioAtualizado);
    }

    @Transactional
    public void updateSenha(UUID usuarioId, PasswordUpdateRequestDTO requestDTO) {
        if (!requestDTO.getNovaSenha().equals(requestDTO.getConfirmarNovaSenha())) {
            throw new IllegalArgumentException("A nova senha e a confirmação não coincidem.");
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));

        if (!passwordEncoder.matches(requestDTO.getSenhaAtual(), usuario.getPassword())) {
            throw new IllegalArgumentException("A senha atual está incorreta.");
        }

        usuario.setHashSenha(passwordEncoder.encode(requestDTO.getNovaSenha()));
        usuarioRepository.save(usuario);
    }
}
