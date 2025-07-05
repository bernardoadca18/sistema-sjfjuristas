package com.sjfjuristas.plataforma.backend.dto.ParcelaEmprestimo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.sjfjuristas.plataforma.backend.domain.ParcelaEmprestimo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    public ParcelaEmprestimoResponseDTO(ParcelaEmprestimo entity)
    {
        this.id = entity.getId();
        this.numeroParcela = entity.getNumeroParcela();
        this.dataVencimento = entity.getDataVencimento();
        this.valorTotalParcela = entity.getValorTotalParcela();
        this.statusPagamentoParcelaNome = entity.getStatusPagamentoParcelaIdStatuspagamentoparcela().getNomeStatus();
        this.pixCopiaCola = entity.getPixCopiaCola();
        this.pixQrCodeBase64 = entity.getPixQrCodeBase64();
    }
}