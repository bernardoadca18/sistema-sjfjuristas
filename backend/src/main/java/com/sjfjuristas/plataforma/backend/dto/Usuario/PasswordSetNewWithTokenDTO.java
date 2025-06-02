package com.sjfjuristas.plataforma.backend.dto.Usuario;

import jakarta.validation.constraints.NotNull;
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
    @NotNull private String token;
    @NotNull private String novaSenha;
    @NotNull private String confirmarNovaSenha;
}