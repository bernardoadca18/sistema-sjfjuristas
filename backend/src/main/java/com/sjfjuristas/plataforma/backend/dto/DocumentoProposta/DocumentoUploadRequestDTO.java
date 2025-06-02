package com.sjfjuristas.plataforma.backend.dto.DocumentoProposta;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocumentoUploadRequestDTO {

    @NotNull(message = "O ID da proposta é obrigatório.")
    private UUID propostaId;

    @NotNull(message = "O ID do tipo de documento é obrigatório.")
    private Long tipoDocumentoId;

    @NotNull(message = "O arquivo é obrigatório.")
    private MultipartFile arquivo;
}