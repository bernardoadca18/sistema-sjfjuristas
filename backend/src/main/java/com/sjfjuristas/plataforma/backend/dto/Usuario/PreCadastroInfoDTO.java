package com.sjfjuristas.plataforma.backend.dto.Usuario;

import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.AllArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PreCadastroInfoDTO
{
    private UUID usuarioId;
    private String nomeCompleto;
    private String email;
}
