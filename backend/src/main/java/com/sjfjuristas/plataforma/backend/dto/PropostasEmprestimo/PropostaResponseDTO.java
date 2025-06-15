package com.sjfjuristas.plataforma.backend.dto.PropostasEmprestimo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class PropostaResponseDTO {

    private UUID id;
    private BigDecimal valorSolicitado;
    private String nomeCompletoSolicitante;
    private String emailSolicitante;
    private OffsetDateTime dataSolicitacao;
    private String statusProposta;

    public PropostaResponseDTO(UUID id, BigDecimal valorSolicitado, String nomeCompletoSolicitante, String emailSolicitante, OffsetDateTime dataSolicitacao, String statusProposta) {
        this.id = id;
        this.valorSolicitado = valorSolicitado;
        this.nomeCompletoSolicitante = nomeCompletoSolicitante;
        this.emailSolicitante = emailSolicitante;
        this.dataSolicitacao = dataSolicitacao;
        this.statusProposta = statusProposta;
    }
}