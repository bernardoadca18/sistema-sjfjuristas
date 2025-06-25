package com.sjfjuristas.plataforma.backend.dto.Admin;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.AllArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminUpdatePasswordWithTokenDTO
{
    private String token;
    private String novaSenha;
    private String confirmarNovaSenha;
}
