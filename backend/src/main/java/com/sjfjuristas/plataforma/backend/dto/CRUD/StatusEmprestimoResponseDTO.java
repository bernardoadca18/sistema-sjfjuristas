package com.sjfjuristas.plataforma.backend.dto.CRUD;

import java.util.UUID;

import com.sjfjuristas.plataforma.backend.domain.StatusEmprestimo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusEmprestimoResponseDTO
{
    private UUID id;
    private String nomeStatus;

    public StatusEmprestimoResponseDTO(StatusEmprestimo entity)
    {
        this.id = entity.getId();
        this.nomeStatus = entity.getNomeStatus();
    }
}