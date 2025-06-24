package com.sjfjuristas.plataforma.backend.dto.PropostasEmprestimo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Optional;

@Data
public class RespostaClienteDTO
{
    @NotNull(message = "A ação (aceitar/recusar/contrapropor) é obrigatória.")
    private AcaoCliente acaoCliente;

    @Positive(message = "O valor da contraproposta deve ser positivo.")
    private BigDecimal valorContraproposta;

    private Integer numParcelasContraproposta;

    private String motivoRecusa;

    public enum AcaoCliente
    {
        ACEITAR,
        RECUSAR,
        CONTRAPROPOR
    }

    public Optional<BigDecimal> getValorContrapropostaOpt() {
        return Optional.ofNullable(valorContraproposta);
    }

    public Optional<Integer> getNumParcelasContrapropostaOpt() {
        return Optional.ofNullable(numParcelasContraproposta);
    }
}
