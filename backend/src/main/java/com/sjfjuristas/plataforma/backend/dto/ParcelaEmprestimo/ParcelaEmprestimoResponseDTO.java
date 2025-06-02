package com.sjfjuristas.plataforma.backend.dto.ParcelaEmprestimo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParcelaEmprestimoResponseDTO {

    private UUID id;
    private Integer numeroParcela;
    private LocalDate dataVencimento;
    private BigDecimal valorTotalParcela;
    private String statusPagamentoParcelaNome;
    private String pixCopiaCola; // Opcional
    private String pixQrCodeBase64; // Opcional
}