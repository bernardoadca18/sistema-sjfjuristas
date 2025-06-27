package com.sjfjuristas.plataforma.backend.dto.CRUD;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

import com.sjfjuristas.plataforma.backend.domain.StatusProposta;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusPropostaResponseDTO
{
    private UUID id;
    String nomeStatus;

    public StatusPropostaResponseDTO(StatusProposta entity)
    {
        this.id = entity.getId();
        this.nomeStatus = entity.getNomeStatus();
    }
}