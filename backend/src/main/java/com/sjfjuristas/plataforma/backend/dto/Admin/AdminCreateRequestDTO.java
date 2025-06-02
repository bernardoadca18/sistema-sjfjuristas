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
public class AdminCreateRequestDTO {
    private String nomeCompleto;
    private String email;
    private String senha;
    private String confirmarSenha;
    private String telefoneContatoInterno;
    private UUID perfilId;
    private String cargoInterno;
    private String departamento;
    private String matriculaFuncionario;
}
