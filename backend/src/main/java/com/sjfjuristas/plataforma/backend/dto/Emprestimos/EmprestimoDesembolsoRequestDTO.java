package com.sjfjuristas.plataforma.backend.dto.Emprestimos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmprestimoDesembolsoRequestDTO {

    @NotNull(message = "O ID da chave PIX para desembolso é obrigatório.")
    private UUID chavePixDesembolsoId;

    @NotNull(message = "O valor a liberar é obrigatório.")
    @Positive(message = "O valor a liberar deve ser positivo.")
    private BigDecimal valorLiberar;
}