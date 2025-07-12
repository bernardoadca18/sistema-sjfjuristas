package com.sjfjuristas.plataforma.backend.dto.PropostasEmprestimo;

import java.math.BigDecimal;
import java.util.Optional;

import com.sjfjuristas.plataforma.backend.domain.enums.AcaoCliente;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RespostaClienteDTO
{
    @NotNull(message = "A ação (aceitar/recusar/contrapropor) é obrigatória.")
    private AcaoCliente acaoCliente;

    @Positive(message = "O valor da contraproposta deve ser positivo.")
    private BigDecimal valorContraproposta;

    private BigDecimal taxaJurosOfertada;

    private Integer numParcelasContraproposta;

    private String motivoRecusa;

    private boolean aceite;
    

    public Optional<BigDecimal> getValorContrapropostaOpt()
    {
        return Optional.ofNullable(valorContraproposta);
    }

    public Optional<Integer> getNumParcelasContrapropostaOpt()
    {
        return Optional.ofNullable(numParcelasContraproposta);
    }

    public boolean getAceite()
    {
        return this.aceite;
    }
}
