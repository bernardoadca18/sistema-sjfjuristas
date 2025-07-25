package com.sjfjuristas.plataforma.backend.dto.DocumentoProposta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocumentoPropostaSummaryDTO {

    private UUID id;
    private String tipoDocumentoNome;
    private String nomeArquivoOriginal;
    private String statusValidacao;
}
