package com.sjfjuristas.plataforma.backend.dto.PropostasEmprestimo;

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
}

