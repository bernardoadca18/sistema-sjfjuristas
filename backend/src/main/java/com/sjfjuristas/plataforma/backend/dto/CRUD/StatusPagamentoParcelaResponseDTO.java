package com.sjfjuristas.plataforma.backend.dto.CRUD;

import java.util.UUID;

import com.sjfjuristas.plataforma.backend.domain.StatusPagamentoParcela;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusPagamentoParcelaResponseDTO
{
    private UUID id;
    private String nomeStatus;

    public StatusPagamentoParcelaResponseDTO(StatusPagamentoParcela entity)
    {
        this.id = entity.getId();
        this.nomeStatus = entity.getNomeStatus();
    }
}