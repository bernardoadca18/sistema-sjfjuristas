package com.sjfjuristas.plataforma.backend.dto.Notificacoes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificacaoResponseDTO {

    private UUID id;
    private String titulo;
    private String mensagem;
    private OffsetDateTime dataCriacao;
    private boolean lida;
    private String tipoNotificacao;
    private String linkDestinoApp; // Opcional
}