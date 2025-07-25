package com.sjfjuristas.plataforma.backend.dto.ComprovantesPagamento;

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
public class ComprovanteResponseDTO {

    private UUID id;
    private String nomeArquivoOriginal;
    private String urlVisualizacao; // Opcional
    private OffsetDateTime dataUpload;
    private String statusVerificacao;
}