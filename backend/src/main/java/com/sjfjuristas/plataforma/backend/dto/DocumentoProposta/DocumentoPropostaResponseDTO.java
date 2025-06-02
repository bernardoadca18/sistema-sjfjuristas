package com.sjfjuristas.plataforma.backend.dto.DocumentoProposta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocumentoPropostaResponseDTO {

    private UUID id;
    private String tipoDocumentoNome;
    private String nomeArquivoOriginal;
    private String urlVisualizacao; // Opcional
    private OffsetDateTime dataUpload;
    private String statusValidacao;
    private String observacoesValidacao; // Opcional
}