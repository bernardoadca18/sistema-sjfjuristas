package com.sjfjuristas.plataforma.backend.service.CRUD;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sjfjuristas.plataforma.backend.domain.PerfilUsuario;
import com.sjfjuristas.plataforma.backend.dto.CRUD.PerfilUsuarioRequestDTO;
import com.sjfjuristas.plataforma.backend.dto.CRUD.PerfilUsuarioResponseDTO;
import com.sjfjuristas.plataforma.backend.repository.PerfilUsuarioRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PerfilUsuarioCRUDService
{
    @Autowired
    private PerfilUsuarioRepository perfilUsuarioRepository;

    @Transactional(readOnly = true)
    public List<PerfilUsuarioResponseDTO> getAllPerfisUsuarios()
    {
        return perfilUsuarioRepository.findAll().stream().map(PerfilUsuarioResponseDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public Page<PerfilUsuarioResponseDTO> getAllPerfisUsuarios(Pageable pageable)
    {
        Page<PerfilUsuario> perfis = perfilUsuarioRepository.findAll(pageable);
        return perfis.map(PerfilUsuarioResponseDTO::new);
    }

    @Transactional(readOnly = true)
    public PerfilUsuarioResponseDTO getPerfilUsuarioById(UUID id)
    {
        PerfilUsuario perfilUsuario = perfilUsuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Perfil de usuário não encontrado"));
        return new PerfilUsuarioResponseDTO(perfilUsuario);
    }

    @Transactional(readOnly = true)
    public PerfilUsuarioResponseDTO getPerfilUsuarioByNomePerfil(String nomePerfil)
    {
        PerfilUsuario perfilUsuario = perfilUsuarioRepository.findByNomePerfil(nomePerfil).orElseThrow(() -> new RuntimeException("Perfil de usuário com nome '" + nomePerfil + "' não encontrado"));
        return new PerfilUsuarioResponseDTO(perfilUsuario);
    }

    @Transactional
    public PerfilUsuarioResponseDTO createPerfilUsuario(PerfilUsuarioRequestDTO dto)
    {
        PerfilUsuario perfilUsuario = new PerfilUsuario();

        perfilUsuario.setNomePerfil(dto.getNomePerfil());

        perfilUsuario = perfilUsuarioRepository.save(perfilUsuario);

        return new PerfilUsuarioResponseDTO(perfilUsuario);
    }

    @Transactional
    public PerfilUsuarioResponseDTO updatePerfilUsuario(UUID id, PerfilUsuarioRequestDTO dto)
    {
        PerfilUsuario perfilUsuario = perfilUsuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Perfil de usuário não encontrado"));

        perfilUsuario.setNomePerfil(dto.getNomePerfil());

        perfilUsuario = perfilUsuarioRepository.save(perfilUsuario);

        return new PerfilUsuarioResponseDTO(perfilUsuario);
    }

    @Transactional
    public void deletePerfilUsuario(UUID id)
    {
        PerfilUsuario perfilUsuario = perfilUsuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Perfil de usuário não encontrado"));
        perfilUsuarioRepository.delete(perfilUsuario);
    }
}