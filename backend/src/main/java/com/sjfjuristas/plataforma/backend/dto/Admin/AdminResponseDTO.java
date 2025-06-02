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
    @NotNull private UUID id;
    @NotNull private String nomeCompleto;
    @NotNull private String email;
    @NotNull private String telefoneContatoInterno;
    @NotNull private UUID perfilId;
    @NotNull private String perfilName;
    @NotNull private String cargoInterno;
    @NotNull private String departamento;
    @NotNull private String matriculaFuncionario;
    @NotNull private String dataCriacao;
    @NotNull private boolean ativo;
}
