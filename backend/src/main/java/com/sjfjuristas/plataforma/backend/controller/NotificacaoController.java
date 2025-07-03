package com.sjfjuristas.plataforma.backend.controller;

import com.sjfjuristas.plataforma.backend.domain.Usuario;
import com.sjfjuristas.plataforma.backend.dto.Notificacoes.NotificacaoReadUpdateRequestDTO;
import com.sjfjuristas.plataforma.backend.dto.Notificacoes.NotificacaoResponseDTO;
import com.sjfjuristas.plataforma.backend.service.NotificacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cliente/notificacoes")
public class NotificacaoController 
{
    @Autowired
    private NotificacaoService notificacaoService;

    @GetMapping
    public ResponseEntity<List<NotificacaoResponseDTO>> getMinhasNotificacoes(@AuthenticationPrincipal Usuario usuarioLogado)
    {
        List<NotificacaoResponseDTO> notificacoes = notificacaoService.getNotificacoesDoUsuario(usuarioLogado);
        return ResponseEntity.ok(notificacoes);
    }

    @PatchMapping("/marcar-como-lidas")
    public ResponseEntity<Void> marcarComoLidas(@AuthenticationPrincipal Usuario usuarioLogado, @RequestBody NotificacaoReadUpdateRequestDTO request)
    {
        notificacaoService.marcarComoLidas(request.getIdsNotificacoes(), usuarioLogado);
        return ResponseEntity.noContent().build();
    }
}