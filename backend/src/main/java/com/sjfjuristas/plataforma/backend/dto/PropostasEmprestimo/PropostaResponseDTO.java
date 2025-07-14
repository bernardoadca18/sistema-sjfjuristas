package com.sjfjuristas.plataforma.backend.dto.PropostasEmprestimo;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sjfjuristas.plataforma.backend.domain.PropostaEmprestimo;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@Getter
@Setter
public class PropostaResponseDTO
{
    private UUID id;
    private BigDecimal valorProposta;
    private String nomeCompletoSolicitante;
    private String emailSolicitante;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private OffsetDateTime dataSolicitacao;
    
    private String statusProposta;

    public PropostaResponseDTO(PropostaEmprestimo entity)
    {
        this.id = entity.getId();
        this.valorProposta = entity.getValorSolicitado();
        this.nomeCompletoSolicitante = entity.getNomeCompletoSolicitante();
        this.emailSolicitante = entity.getEmailSolicitante();
        this.dataSolicitacao = entity.getDataSolicitacao();
        this.statusProposta = entity.getStatusPropostaIdStatusproposta().getNomeStatus();
    }
}