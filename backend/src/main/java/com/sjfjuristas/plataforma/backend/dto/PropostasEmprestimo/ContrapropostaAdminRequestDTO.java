package com.sjfjuristas.plataforma.backend.dto.PropostasEmprestimo;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ContrapropostaAdminRequestDTO
{
    @NotNull
    @Positive
    private BigDecimal valorOfertado;

    @NotNull
    @Positive
    private BigDecimal taxaJurosOfertada;

    @NotNull
    @Min(value = 30, message = "O número mínimo de parcelas é 30.")
    @Max(value = 180, message = "O número máximo de parcelas é 180.")
    private Integer numParcelasOfertado;

    @NotNull
    @Future(message = "A data prevista para depósito deve ser no futuro.")
    private LocalDate dataDepositoPrevista;
    
    @NotNull
    @Future(message = "A data de início do pagamento deve ser no futuro.")
    private LocalDate dataInicioPagamentoPrevista;
}
