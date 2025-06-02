package com.sjfjuristas.plataforma.backend.dto.ChavesPixUsuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChavePixCreateRequestDTO {

    @NotNull(message = "O ID do tipo da chave PIX é obrigatório.")
    private Long tipoChavePixId;

    @NotBlank(message = "O valor da chave PIX é obrigatório.")
    private String valorChave;
}