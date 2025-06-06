package com.sjfjuristas.plataforma.backend.service.impl;

import com.sjfjuristas.plataforma.backend.domain.Usuario;
import com.sjfjuristas.plataforma.backend.dto.Usuario.ClienteResponseDTO;
import com.sjfjuristas.plataforma.backend.dto.Usuario.ClienteUpdateRequestDTO;
import com.sjfjuristas.plataforma.backend.dto.Usuario.PasswordUpdateRequestDTO;
import com.sjfjuristas.plataforma.backend.repository.UsuarioRepository;
import com.sjfjuristas.plataforma.backend.service.ClienteService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public ClienteResponseDTO getClienteInfo(UUID usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com ID: " + usuarioId));
        
        // Mapear para DTO
        return ClienteResponseDTO.builder()
                .id(usuario.getId())
                .nomeCompleto(usuario.getNomeCompleto())
                .cpf(usuario.getCpf()) // Considere mascarar o CPF aqui ou no controller/frontend
                .email(usuario.getEmail())
                .telefoneWhatsapp(usuario.getTelefoneWhatsapp())
                .dataNascimento(usuario.getDataNascimento() != null ? usuario.getDataNascimento().toString() : null)
                .dataCadastro(usuario.getDataCadastro() != null ? usuario.getDataCadastro().toString() : null)
                .emailVerificado(Boolean.TRUE.equals(usuario.getEmailVerificado()))
                .build();
    }

    @Override
    @Transactional
    public ClienteResponseDTO updateClienteInfo(UUID usuarioId, ClienteUpdateRequestDTO request) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com ID: " + usuarioId));

        // Atualizar campos permitidos
        if (request.getNomeCompleto() != null) {
            usuario.setNomeCompleto(request.getNomeCompleto());
        }
        if (request.getTelefoneWhatsapp() != null) {
            usuario.setTelefoneWhatsapp(request.getTelefoneWhatsapp());
        }
        if (request.getDataNascimento() != null) {
            usuario.setDataNascimento(LocalDate.parse(request.getDataNascimento()));
        }
        if (request.getEnderecoCompleto() != null) {
            usuario.setEnderecoCompleto(request.getEnderecoCompleto());
        }

        Usuario usuarioAtualizado = usuarioRepository.save(usuario);
        return getClienteInfo(usuarioAtualizado.getId()); // Reutiliza o método para mapear para DTO
    }

    @Override
    @Transactional
    public void updatePassword(UUID usuarioId, PasswordUpdateRequestDTO request) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com ID: " + usuarioId));

        // 1. Verificar se a senha atual corresponde
        if (!passwordEncoder.matches(request.getSenhaAtual(), usuario.getHashSenha())) {
            throw new IllegalArgumentException("Senha atual incorreta.");
        }
        // 2. Verificar se a nova senha e a confirmação coincidem
        if (!request.getNovaSenha().equals(request.getConfirmarNovaSenha())) {
            throw new IllegalArgumentException("A nova senha e a confirmação não coincidem.");
        }
        // 3. (Opcional) Validar força da nova senha

        // Atualizar o hash da senha
        usuario.setHashSenha(passwordEncoder.encode(request.getNovaSenha()));
        usuarioRepository.save(usuario);
    }
}