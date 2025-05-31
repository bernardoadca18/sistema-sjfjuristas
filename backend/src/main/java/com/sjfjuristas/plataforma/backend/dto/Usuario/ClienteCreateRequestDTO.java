package com.sjfjuristas.plataforma.backend.dto.Usuario;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClienteCreateRequestDTO {
    @NotNull
    private String nomeCompleto;
    @NotNull
    private String cpf;
    @NotNull
    private String email;
    @NotNull
    private String senha;
    @NotNull
    private String confirmarSenha;
    @NotNull
    private String telefoneWhatsapp;
    @NotNull
    private String dataNascimento; // Formato ISO 8601 (YYYY-MM-DD)
    @NotNull
    private boolean aceitouTermos;
    private String enderecoCompleto;
}
