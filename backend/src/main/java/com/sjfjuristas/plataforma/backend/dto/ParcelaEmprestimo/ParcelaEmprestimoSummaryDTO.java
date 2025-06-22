package com.sjfjuristas.plataforma.backend.dto.ParcelaEmprestimo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

import com.sjfjuristas.plataforma.backend.domain.ParcelaEmprestimo;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParcelaEmprestimoSummaryDTO
{

    private Integer numeroParcela;
    private LocalDate dataVencimento;
    private BigDecimal valorTotalParcela;
    private String statusPagamentoParcelaNome;

    public ParcelaEmprestimoSummaryDTO(ParcelaEmprestimo parcela)
    {
        this.numeroParcela = parcela.getNumeroParcela();
        this.dataVencimento = parcela.getDataVencimento();
        this.valorTotalParcela = parcela.getValorTotalParcela();
        if (parcela.getStatusPagamentoParcelaIdStatuspagamentoparcela() != null)
        {
            this.statusPagamentoParcelaNome = parcela.getStatusPagamentoParcelaIdStatuspagamentoparcela().getNomeStatus();
        }
    }
}