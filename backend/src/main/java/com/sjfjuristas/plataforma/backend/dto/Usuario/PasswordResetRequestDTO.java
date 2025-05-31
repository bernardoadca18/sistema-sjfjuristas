package com.sjfjuristas.plataforma.backend.dto.Usuario;

import jakarta.validation.constraints.NotNull;

public class PasswordResetRequestDTO {
    @NotNull
    private String email;
}
