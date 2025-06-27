package com.sjfjuristas.plataforma.backend.dto.CRUD;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OcupacaoRequestDTO
{
    private String nomeOcupacao;
    private boolean ativo;
}