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
public class PagamentoParcelaAdminRequestDTO {
    private UUID parcelaId;
    private BigDecimal valorPago;
    private OffsetDateTime dataPagamentoEfetivo;
    private String meioPagamento;
    private String idTransacaoPsp; // Opcional
    private String observacoes; // Opcional
}