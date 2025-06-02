package com.sjfjuristas.plataforma.backend.dto.Usuario;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.AllArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordSetNewWithTokenDTO 
{
    private String token;
    private String novaSenha;
    private String confirmarNovaSenha;
}