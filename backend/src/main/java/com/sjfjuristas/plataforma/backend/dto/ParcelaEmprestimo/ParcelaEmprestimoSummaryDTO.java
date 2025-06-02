package com.sjfjuristas.plataforma.backend.dto.ParcelaEmprestimo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParcelaEmprestimoSummaryDTO {

    private Integer numeroParcela;
    private LocalDate dataVencimento;
    private BigDecimal valorTotalParcela;
    private String statusPagamentoParcelaNome;
}