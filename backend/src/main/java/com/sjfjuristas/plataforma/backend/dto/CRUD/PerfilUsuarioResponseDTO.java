package com.sjfjuristas.plataforma.backend.dto.CRUD;

import java.util.UUID;

import com.sjfjuristas.plataforma.backend.domain.PerfilUsuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerfilUsuarioResponseDTO
{
    private UUID id;
    private String nomePerfil;

    public PerfilUsuarioResponseDTO(PerfilUsuario entity)
    {
        this.id = entity.getId();
        this.nomePerfil = entity.getNomePerfil();
    }
}