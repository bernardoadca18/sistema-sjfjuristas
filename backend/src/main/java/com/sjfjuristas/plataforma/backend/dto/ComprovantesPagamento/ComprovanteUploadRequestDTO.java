package com.sjfjuristas.plataforma.backend.dto.ComprovantesPagamento;

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
public class ComprovanteUploadRequestDTO {

    @NotNull(message = "O ID da parcela é obrigatório.")
    private UUID parcelaId;

    @NotNull(message = "O arquivo do comprovante é obrigatório.")
    private MultipartFile arquivo;
}