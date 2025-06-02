package com.sjfjuristas.plataforma.backend.dto.Admin;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminCreateRequestDTO {
    @NotNull private String nomeCompleto;
    @NotNull private String email;
    @NotNull private String senha;
    @NotNull private String confirmarSenha;
    private String telefoneContatoInterno;
    private UUID perfilId;
    private String cargoInterno;
    private String departamento;
    private String matriculaFuncionario;
}
