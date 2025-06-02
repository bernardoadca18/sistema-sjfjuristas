package com.sjfjuristas.plataforma.backend.dto.DocumentoProposta;

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
    private UUID propostaId;
    private Long tipoDocumentoId;
    private MultipartFile arquivo;
}