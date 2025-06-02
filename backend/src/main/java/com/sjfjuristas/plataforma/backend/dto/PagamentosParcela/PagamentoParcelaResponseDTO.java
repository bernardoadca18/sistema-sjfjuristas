package com.sjfjuristas.plataforma.backend.dto.PagamentosParcela;

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
public class PagamentoParcelaResponseDTO {

    private UUID id;
    private OffsetDateTime dataPagamentoEfetivo;
    private BigDecimal valorPago;
    private String statusPagamentoAplicado;
    private String referenciaParcela; // Ex: "Parcela 5 de 20/07/2025"
    private String meioPagamento;
}