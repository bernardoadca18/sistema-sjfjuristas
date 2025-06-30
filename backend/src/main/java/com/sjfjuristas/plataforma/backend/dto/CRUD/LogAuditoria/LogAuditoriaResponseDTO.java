package com.sjfjuristas.plataforma.backend.dto.CRUD.LogAuditoria;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
// Retornar os dados de um log de auditoria para exibição na dashboard. Usado em GET /api/admin/auditoria.
public class LogAuditoriaResponseDTO
{
    private UUID id;
    private LocalDateTime timestamp;
    private String acao;
    private String detalhes;
    private UUID adminId;
    private String nomeAdmin;
}
