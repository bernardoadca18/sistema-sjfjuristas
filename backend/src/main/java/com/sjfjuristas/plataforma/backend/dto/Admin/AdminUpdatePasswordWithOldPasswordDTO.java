package com.sjfjuristas.plataforma.backend.dto.Admin;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.AllArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminUpdatePasswordWithOldPasswordDTO
{
    private String senhaAntiga;
    private String novaSenha;
    private String confirmarNovaSenha;
}
