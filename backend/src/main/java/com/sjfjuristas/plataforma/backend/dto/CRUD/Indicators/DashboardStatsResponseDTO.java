package com.sjfjuristas.plataforma.backend.dto.CRUD.Indicators;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
// Retornar os principais KPIs para a tela inicial da dashboard.
// GET /api/admin/dashboard/estatisticas
public class DashboardStatsResponseDTO
{
    private Long totalPropostasPendentes;
    private Long totalPropostasAprovadasMes;
    private Long totalEmprestimosAtivos;
    private BigDecimal valorTotalDesembolsado;
    private Long totalParcelasAtrasadas;
    private BigDecimal valorRecebidoHoje;
    private Long novosClientesMes;
}