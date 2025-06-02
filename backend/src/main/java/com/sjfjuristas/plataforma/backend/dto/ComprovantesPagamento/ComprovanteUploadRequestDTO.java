package com.sjfjuristas.plataforma.backend.dto.ComprovantesPagamento;

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
public class ComprovanteUploadRequestDTO {
    private UUID parcelaId;
    private MultipartFile arquivo;
}