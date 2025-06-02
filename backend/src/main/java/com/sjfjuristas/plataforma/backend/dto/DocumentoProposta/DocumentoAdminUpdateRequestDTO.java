package com.sjfjuristas.plataforma.backend.dto.DocumentoProposta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocumentoAdminUpdateRequestDTO {
    private String statusValidacao;
    private String observacoesValidacao; // Opcional
}