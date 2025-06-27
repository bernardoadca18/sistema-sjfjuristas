package com.sjfjuristas.plataforma.backend.dto.CRUD;

import java.util.UUID;

import com.sjfjuristas.plataforma.backend.domain.TipoChavePix;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoChavePixResponseDTO
{
    private UUID id;
    private String nomeTipo;

    public TipoChavePixResponseDTO(TipoChavePix entity)
    {
        this.id = entity.getId();
        this.nomeTipo = entity.getNomeTipo();
    }
}