package com.sjfjuristas.plataforma.backend.dto.DocumentoProposta;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocumentoAdminUpdateRequestDTO {

    @NotBlank(message = "O novo status de validação é obrigatório.")
    private String statusValidacao;
    private String observacoesValidacao; // Opcional
}