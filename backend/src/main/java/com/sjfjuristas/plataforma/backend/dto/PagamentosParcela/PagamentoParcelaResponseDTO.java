package com.sjfjuristas.plataforma.backend.dto.PagamentosParcela;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import com.sjfjuristas.plataforma.backend.domain.PagamentoParcela;
import com.sjfjuristas.plataforma.backend.domain.ParcelaEmprestimo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    public PagamentoParcelaResponseDTO(PagamentoParcela entity)
    {
        this.id = entity.getId();
        this.dataPagamentoEfetivo = entity.getDataPagamentoEfetivo();
        this.valorPago = entity.getValorPago();
        this.statusPagamentoAplicado = "";
        this.referenciaParcela = generateReferenciaParcela(entity.getParcelaIdParcelasemprestimo());
        this.meioPagamento = entity.getMeioPagamento();
    }

    private String generateReferenciaParcela(ParcelaEmprestimo parcela)
    {
        String parcelaRef = "Parcela " + parcela.getNumeroParcela() + " de " + parcela.getDataVencimento();
        return parcelaRef;
    }
}