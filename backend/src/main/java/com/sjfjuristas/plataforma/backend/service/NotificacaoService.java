package com.sjfjuristas.plataforma.backend.service;

import com.sjfjuristas.plataforma.backend.domain.Notificacao;
import com.sjfjuristas.plataforma.backend.domain.Usuario;
import com.sjfjuristas.plataforma.backend.dto.Notificacoes.NotificacaoResponseDTO;
import com.sjfjuristas.plataforma.backend.repository.NotificacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class NotificacaoService 
{
    @Autowired
    private NotificacaoRepository notificacaoRepository;

    @Transactional(readOnly = true)
    public List<NotificacaoResponseDTO> getNotificacoesDoUsuario(Usuario usuario) {
        return notificacaoRepository.findByUsuarioIdUsuariosOrderByDataCriacaoDesc(usuario)
                .stream().map(this::toResponseDto).collect(Collectors.toList());
    }

    @Transactional
    public void marcarComoLidas(List<UUID> notificacaoIds, Usuario usuario) {
        List<Notificacao> notificacoes = notificacaoRepository.findAllById(notificacaoIds);
        for (Notificacao notificacao : notificacoes) {
            if (notificacao.getUsuarioIdUsuarios().getId().equals(usuario.getId())) {
                notificacao.setLida(true);
                notificacao.setDataLeitura(OffsetDateTime.now());
            }
        }
        notificacaoRepository.saveAll(notificacoes);
    }
    
    private NotificacaoResponseDTO toResponseDto(Notificacao notificacao) {
        return new NotificacaoResponseDTO(
            notificacao.getId(),
            notificacao.getTitulo(),
            notificacao.getMensagem(),
            notificacao.getDataCriacao(),
            notificacao.getLida(),
            notificacao.getTipoNotificacao(),
            notificacao.getLinkDestinoApp()
        );
    }
}