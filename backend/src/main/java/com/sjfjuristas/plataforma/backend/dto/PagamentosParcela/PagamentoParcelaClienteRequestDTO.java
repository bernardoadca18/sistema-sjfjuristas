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
public class PagamentoParcelaClienteRequestDTO {
    private UUID parcelaId;
    // Opcionais, pois o sistema pode obter os dados via webhook do PSP
    private String idTransacaoPsp;
    private BigDecimal valorPago;
    private OffsetDateTime dataPagamento;
}
