package com.sjfjuristas.plataforma.backend.dto.Emprestimos;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import com.sjfjuristas.plataforma.backend.domain.Emprestimo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmprestimoSummaryDTO
{
    private UUID id;
    private BigDecimal valor;
    private Integer numeroParcelas;
    private OffsetDateTime dataContratacao;

    public EmprestimoSummaryDTO(Emprestimo entity)
    {
        this.id = entity.getId();
        this.valor = entity.getValorLiberado();
        this.numeroParcelas = entity.getNumeroTotalParcelas();
        this.dataContratacao = entity.getDataContratacao();
    }
}
