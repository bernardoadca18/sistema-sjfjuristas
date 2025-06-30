package com.sjfjuristas.plataforma.backend.dto.CRUD;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
// Permitir que o administrador registre um pagamento de parcela manualmente.
// POST /api/admin/parcelas/{parcelaId}/registrar-pagamento-manual
// O comprovante (arquivo) deve ser tratado como MultipartFile no controller
public class PagamentoManualAdminRequestDTO
{
    private LocalDate dataPagamento;
    private BigDecimal valorPago;
    private UUID statusPagamentoId;
    private String observacao;
}
