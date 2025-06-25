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
public class AdminUpdatePasswordRequestDTO
{
    private String senhaAtual;
    private String novaSenha;
    private String confirmarNovaSenha;
}
