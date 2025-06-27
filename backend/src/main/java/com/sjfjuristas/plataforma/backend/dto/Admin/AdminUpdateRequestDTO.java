package com.sjfjuristas.plataforma.backend.dto.Admin;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.AllArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminUpdateRequestDTO
{
    private String nomeCompleto;
    private String telefoneContatoInterno;
    private String cargoInterno;
    private String departamento;
    private String matriculaFuncionario;
    private boolean ativo;
}
