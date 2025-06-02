package com.sjfjuristas.plataforma.backend.dto.PagamentosParcela;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PagamentoParcelaAdminRequestDTO {

    @NotNull(message = "O ID da parcela é obrigatório.")
    private UUID parcelaId;

    @NotNull(message = "O valor pago é obrigatório.")
    @Positive(message = "O valor pago deve ser positivo.")
    private BigDecimal valorPago;

    @NotNull(message = "A data efetiva do pagamento é obrigatória.")
    private OffsetDateTime dataPagamentoEfetivo;

    @NotBlank(message = "O meio de pagamento é obrigatório.")
    private String meioPagamento;

    private String idTransacaoPsp; // Opcional
    private String observacoes; // Opcional
}