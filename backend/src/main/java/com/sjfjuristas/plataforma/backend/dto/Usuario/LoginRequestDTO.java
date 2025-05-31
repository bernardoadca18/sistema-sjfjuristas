package com.sjfjuristas.plataforma.backend.dto.Usuario;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDTO {
    @NotNull
    private String email;
    @NotNull
    private String senha;
}
