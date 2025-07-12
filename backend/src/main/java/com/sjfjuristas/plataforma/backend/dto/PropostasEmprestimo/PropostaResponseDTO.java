package com.sjfjuristas.plataforma.backend.dto.PropostasEmprestimo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import com.sjfjuristas.plataforma.backend.domain.PropostaEmprestimo;

@Data
@NoArgsConstructor
@Getter
@Setter
public class PropostaResponseDTO
{
    private UUID id;
    private BigDecimal valorSolicitado;
    private String nomeCompletoSolicitante;
    private String emailSolicitante;
    private OffsetDateTime dataSolicitacao;
    private String statusProposta;

    public PropostaResponseDTO(UUID id, BigDecimal valorSolicitado, String nomeCompletoSolicitante, String emailSolicitante, OffsetDateTime dataSolicitacao, String statusProposta)
    {
        this.id = id;
        this.valorSolicitado = valorSolicitado;
        this.nomeCompletoSolicitante = nomeCompletoSolicitante;
        this.emailSolicitante = emailSolicitante;
        this.dataSolicitacao = dataSolicitacao;
        this.statusProposta = statusProposta;
    }

    public PropostaResponseDTO(PropostaEmprestimo entity)
    {
        this.id = entity.getId();
        this.valorSolicitado = entity.getValorSolicitado();
        this.nomeCompletoSolicitante = entity.getNomeCompletoSolicitante();
        this.emailSolicitante = entity.getEmailSolicitante();
        this.dataSolicitacao = entity.getDataSolicitacao();
        this.statusProposta = entity.getStatusPropostaIdStatusproposta().getNomeStatus();
    }
}