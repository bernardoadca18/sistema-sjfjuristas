package com.sjfjuristas.plataforma.backend.dto.PropostasEmprestimo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;
import com.sjfjuristas.plataforma.backend.domain.PropostaHistorico;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PropostaHistoricoResponseDTO
{
    private UUID id;
    private OffsetDateTime dataAlteracao;
    private String atorAlteracao;
    private String statusAnterior;
    private String statusNovo;
    private BigDecimal valorAnterior;
    private BigDecimal valorNovo;
    private Integer numParcelasAnterior;
    private Integer numParcelasNovo;
    private BigDecimal taxaJurosAnterior;
    private BigDecimal taxaJurosNova;
    private String motivoRecusa;
    private String observacoes;

    public PropostaHistoricoResponseDTO(PropostaHistorico entity)
    {
        this.id = entity.getId();
        this.dataAlteracao = entity.getDataAlteracao();
        this.atorAlteracao = entity.getAtorAlteracao();
        this.statusAnterior = entity.getStatusAnterior();
        this.statusNovo = entity.getStatusNovo();
        this.valorAnterior = entity.getValorAnterior();
        this.valorNovo = entity.getValorNovo();
        this.numParcelasAnterior = entity.getNumParcelasAnterior();
        this.numParcelasNovo = entity.getNumParcelasNovo();
        this.taxaJurosAnterior = entity.getTaxaJurosAnterior();
        this.taxaJurosNova = entity.getTaxaJurosNova();
        this.motivoRecusa = entity.getMotivoRecusa();
        this.observacoes = entity.getObservacoes();
    }
}

