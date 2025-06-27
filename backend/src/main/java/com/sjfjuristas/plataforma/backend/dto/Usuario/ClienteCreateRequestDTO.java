package com.sjfjuristas.plataforma.backend.dto.Usuario;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.AllArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClienteCreateRequestDTO
{
    private String nomeCompleto;
    private String cpf;
    private String email;
    private String senha;
    private String confirmarSenha;
    private String telefoneWhatsapp;
    private String dataNascimento; // Formato ISO 8601 (YYYY-MM-DD)
    private boolean aceitouTermos;
    private String enderecoCompleto;
}