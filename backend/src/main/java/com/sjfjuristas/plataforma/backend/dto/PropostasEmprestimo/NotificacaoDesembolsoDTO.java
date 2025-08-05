package com.sjfjuristas.plataforma.backend.dto.PropostasEmprestimo;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NotificacaoDesembolsoDTO
{
    private UUID usuarioId;
    private BigDecimal valorDesembolsado;
}
