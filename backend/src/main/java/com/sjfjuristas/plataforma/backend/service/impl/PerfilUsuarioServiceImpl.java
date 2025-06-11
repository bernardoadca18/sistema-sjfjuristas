package com.sjfjuristas.plataforma.backend.service.impl;

import com.sjfjuristas.plataforma.backend.domain.PerfilUsuario;
import com.sjfjuristas.plataforma.backend.dto.PerfilUsuario.PerfilUsuarioDTO;
import com.sjfjuristas.plataforma.backend.repository.PerfilUsuarioRepository;
import com.sjfjuristas.plataforma.backend.repository.UsuarioRepository;
//import com.sjfjuristas.plataforma.backend.repository.UsuarioRepository;
import com.sjfjuristas.plataforma.backend.service.PerfilUsuarioService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PerfilUsuarioServiceImpl implements PerfilUsuarioService {

    private final PerfilUsuarioRepository perfilUsuarioRepository;
    private final UsuarioRepository usuarioRepository; // Para validar se o perfil está em uso

    @Override
    @Transactional
    public PerfilUsuarioDTO criarPerfil(PerfilUsuarioDTO dto) {
        if (perfilUsuarioRepository.findByNomePerfil(dto.getNome()).isPresent()) {
            throw new IllegalArgumentException("Já existe um perfil com este nome.");
        }
        PerfilUsuario perfil = new PerfilUsuario();
        perfil.setNomePerfil(dto.getNome());
        return mapToDTO(perfilUsuarioRepository.save(perfil));
    }

    @Override
    @Transactional(readOnly = true)
    public PerfilUsuarioDTO getPerfilById(UUID id) {
        return perfilUsuarioRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new EntityNotFoundException("Perfil não encontrado com ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PerfilUsuarioDTO> getAllPerfis(Pageable pageable) {
        return perfilUsuarioRepository.findAll(pageable).map(this::mapToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PerfilUsuarioDTO> getAllPerfisList() {
        return perfilUsuarioRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PerfilUsuarioDTO updatePerfil(UUID id, PerfilUsuarioDTO dto) {
        PerfilUsuario perfil = perfilUsuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Perfil não encontrado com ID: " + id));

        if (!perfil.getNomePerfil().equalsIgnoreCase(dto.getNome()) &&
            perfilUsuarioRepository.findByNomePerfil(dto.getNome()).isPresent()) {
            throw new IllegalArgumentException("Já existe um perfil com o novo nome fornecido.");
        }
        
        perfil.setNomePerfil(dto.getNome());
        return mapToDTO(perfilUsuarioRepository.save(perfil));
    }

    private PerfilUsuarioDTO mapToDTO(PerfilUsuario perfil) {
        return PerfilUsuarioDTO.builder()
                .id(perfil.getId())
                .nome(perfil.getNomePerfil())
                .build();
    }

    @Override
    @Transactional
    public void deletePerfil(UUID id) {
        PerfilUsuario perfil = perfilUsuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Perfil não encontrado com ID: " + id));
        
        // **Validação Crítica**: Verificar se algum usuário está usando este perfil
        if (usuarioRepository.existsByPerfilIdPerfisusuario(perfil)) { // Ajuste o nome do método no repo
            throw new IllegalStateException("Não é possível deletar o perfil, pois ele está em uso por um ou mais usuários.");
        }
        
        perfilUsuarioRepository.deleteById(id);
    }

}