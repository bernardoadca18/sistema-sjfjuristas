package com.sjfjuristas.plataforma.backend.dto.CRUD;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoDocumentoRequestDTO
{
    private String nomeDocumento;
    private String descricao;
    private boolean obrigatorioProposta;
}