package com.sjfjuristas.plataforma.backend.dto.ComprovantesPagamento;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ComprovanteAdminUpdateRequestDTO {

    @NotBlank(message = "O status da verificação é obrigatório.")
    private String statusVerificacao;
    
    private String observacoesVerificacao; // Opcional
}