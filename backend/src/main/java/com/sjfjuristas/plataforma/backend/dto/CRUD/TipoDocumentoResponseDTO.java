package com.sjfjuristas.plataforma.backend.dto.CRUD;

import java.util.UUID;

import com.sjfjuristas.plataforma.backend.domain.TipoDocumento;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoDocumentoResponseDTO
{
    private UUID id;
    private String nomeDocumento;
    private String descricao;
    private boolean obrigatorioProposta;

    public TipoDocumentoResponseDTO(TipoDocumento entity)
    {
        this.id = entity.getId();
        this.nomeDocumento = entity.getNomeDocumento();
        this.descricao = entity.getDescricao();
        this.obrigatorioProposta = entity.getObrigatorioProposta();
    }
}