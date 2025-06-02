package com.sjfjuristas.plataforma.backend.dto.Notificacoes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificacaoReadUpdateRequestDTO {
    private List<UUID> idsNotificacoes;
    private Boolean lida;
}