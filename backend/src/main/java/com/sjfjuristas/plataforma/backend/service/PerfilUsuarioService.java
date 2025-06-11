package com.sjfjuristas.plataforma.backend.service;

import com.sjfjuristas.plataforma.backend.dto.PerfilUsuario.PerfilUsuarioDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.UUID;


public interface PerfilUsuarioService 
{
    PerfilUsuarioDTO criarPerfil(PerfilUsuarioDTO dto);
    PerfilUsuarioDTO getPerfilById(UUID id);
    Page<PerfilUsuarioDTO> getAllPerfis(Pageable pageable);
    List<PerfilUsuarioDTO> getAllPerfisList();
    PerfilUsuarioDTO updatePerfil(UUID id, PerfilUsuarioDTO dto);
    void deletePerfil(UUID id);
}
