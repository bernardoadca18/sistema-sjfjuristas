package com.sjfjuristas.plataforma.backend.dto.CRUD;

import java.util.UUID;

import com.sjfjuristas.plataforma.backend.domain.Ocupacao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OcupacaoResponseDTO
{
    private UUID id;
    private String nomeOcupacao;
    private boolean ativo;

    public OcupacaoResponseDTO(Ocupacao entity)
    {
        this.id = entity.getId();
        this.nomeOcupacao = entity.getNomeOcupacao();
        this.ativo = entity.getAtivo();
    }
}