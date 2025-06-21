package com.sjfjuristas.plataforma.backend.dto.Emprestimos;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CondicoesAprovadasDTO {

    @NotNull(message = "O valor contratado é obrigatório.")
    @Positive(message = "O valor contratado deve ser positivo.")
    private BigDecimal valorContratado;

    @NotNull(message = "O valor liberado é obrigatório.")
    @Positive(message = "O valor liberado deve ser positivo.")
    private BigDecimal valorLiberado;

    @NotNull(message = "A taxa de juros diária é obrigatória.")
    @Positive(message = "A taxa de juros deve ser positiva.")
    private BigDecimal taxaJurosDiaria;

    @NotNull(message = "O número de parcelas é obrigatório.")
    @Positive(message = "O número de parcelas deve ser positivo.")
    private Integer numeroTotalParcelas;

    @NotNull(message = "A data do primeiro vencimento é obrigatória.")
    @FutureOrPresent(message = "A data do primeiro vencimento não pode ser no passado.")
    private LocalDate dataPrimeiroVencimento;
}