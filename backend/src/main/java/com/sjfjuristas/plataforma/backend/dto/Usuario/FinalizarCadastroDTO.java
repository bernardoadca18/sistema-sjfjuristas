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
public class FinalizarCadastroDTO
{
    private UUID usuarioId;
    private String senha;
    private String confirmarSenha;
    private boolean aceitouTermosApp;
}
