package com.sjfjuristas.plataforma.backend.dto.Usuario;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
import java.util.UUID;
import lombok.AllArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClienteResponseDTO {
    private UUID id;
    private String nomeCompleto;
    private String cpf;
    private String email;
    private String telefoneWhatsapp;
    private String dataNascimento; // Formato ISO 8601 (YYYY-MM-DD)
    private String dataCadastro; // Formato ISO 8601 (YYYY-MM-DDTHH:mm:ssZ)
    private boolean emailVerificado;
}