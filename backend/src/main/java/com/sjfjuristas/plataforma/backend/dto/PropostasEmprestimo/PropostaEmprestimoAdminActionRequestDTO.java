package com.sjfjuristas.plataforma.backend.dto.PropostasEmprestimo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PropostaEmprestimoAdminActionRequestDTO {

    @NotNull(message = "O ID do status da proposta é obrigatório.")
    private Long statusPropostaId;
    
    // Opcional, mas obrigatório se negada
    private String motivoNegacao; 

    // Campos opcionais, preenchidos se o status for "Aprovada"
    private BigDecimal valorAprovado;
    private BigDecimal taxaJurosDiariaEfetiva;
    private Integer numeroTotalParcelas;
    private BigDecimal valorParcelaDiaria;
    private LocalDate dataPrimeiroVencimento;
    private LocalDate dataInicioCobrancaParcelas;
    private String observacoesAnalise;
}