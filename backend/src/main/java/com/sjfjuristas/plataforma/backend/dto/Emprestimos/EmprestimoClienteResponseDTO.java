package com.sjfjuristas.plataforma.backend.dto.Emprestimos;

import com.sjfjuristas.plataforma.backend.domain.Emprestimo;
import com.sjfjuristas.plataforma.backend.dto.ParcelaEmprestimo.ParcelaEmprestimoSummaryDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmprestimoClienteResponseDTO {

    private UUID id;
    private BigDecimal valorContratado;
    private BigDecimal valorLiberado;
    private BigDecimal taxaJurosDiariaEfetiva;
    private Integer numeroTotalParcelas;
    private BigDecimal valorParcelaDiaria;
    private OffsetDateTime dataContratacao;
    private LocalDate dataPrimeiroVencimento;
    private LocalDate dataUltimoVencimento;
    private String statusEmprestimoNome;
    private BigDecimal saldoDevedorAtual;
    private LocalDate dataInicioCobrancaParcelas;
    private ParcelaEmprestimoSummaryDTO proximaParcela; // Opcional

    public EmprestimoClienteResponseDTO(Emprestimo entity)
    {
        this.id = entity.getId();
        this.valorContratado = entity.getValorContratado();
        this.valorLiberado = entity.getValorLiberado();
        this.taxaJurosDiariaEfetiva = entity.getTaxaJurosDiariaEfetiva();
        this.numeroTotalParcelas = entity.getNumeroTotalParcelas();
        this.valorParcelaDiaria = entity.getValorParcelaDiaria();
        this.dataContratacao = entity.getDataContratacao();
        this.dataPrimeiroVencimento = entity.getDataPrimeiroVencimento();
        this.dataUltimoVencimento = entity.getDataUltimoVencimento();
        this.statusEmprestimoNome = entity.getStatusEmprestimoIdStatusemprestimo().getNomeStatus();
        this.saldoDevedorAtual = entity.getSaldoDevedorAtual();
        this.dataInicioCobrancaParcelas = entity.getDataInicioCobrancaParcelas();
    }
}
