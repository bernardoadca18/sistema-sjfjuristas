package com.sjfjuristas.plataforma.backend.dto.Notificacoes;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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

    @NotEmpty(message = "A lista de IDs de notificações não pode ser vazia.")
    private List<UUID> idsNotificacoes;

    @NotNull(message = "O status 'lida' é obrigatório.")
    private Boolean lida;
}