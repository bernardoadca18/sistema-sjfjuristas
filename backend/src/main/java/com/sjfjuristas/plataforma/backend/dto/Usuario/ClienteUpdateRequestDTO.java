package com.sjfjuristas.plataforma.backend.dto.Usuario;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.AllArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClienteUpdateRequestDTO {
    private String nomeCompleto;
    private String telefoneWhatsapp;
    private String dataNascimento; // Formato ISO 8601 (YYYY-MM-DD)
    private String enderecoCompleto;
}
