package com.sjfjuristas.plataforma.backend.dto.Admin;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.AllArgsConstructor;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminResponseDTO {
    private UUID id;
    private String nomeCompleto;
    private String email;
    private String telefoneContatoInterno;
    private UUID perfilId;
    private String perfilName;
    private String cargoInterno;
    private String departamento;
    private String matriculaFuncionario;
    private String dataCriacao;
    private boolean ativo;
}
