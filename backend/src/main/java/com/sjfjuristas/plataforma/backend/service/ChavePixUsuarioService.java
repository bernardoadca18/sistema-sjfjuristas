package com.sjfjuristas.plataforma.backend.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sjfjuristas.plataforma.backend.domain.ChavePixUsuario;
import com.sjfjuristas.plataforma.backend.domain.TipoChavePix;
import com.sjfjuristas.plataforma.backend.domain.Usuario;
import com.sjfjuristas.plataforma.backend.dto.ChavesPixUsuario.ChavePixCreateRequestDTO;
import com.sjfjuristas.plataforma.backend.dto.ChavesPixUsuario.ChavePixResponseDTO;
import com.sjfjuristas.plataforma.backend.repository.ChavePixUsuarioRepository;
import com.sjfjuristas.plataforma.backend.repository.TipoChavePixRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ChavePixUsuarioService {

    @Autowired
    private ChavePixUsuarioRepository chavePixRepository;

    @Autowired
    private TipoChavePixRepository tipoChavePixRepository;

    @Transactional(readOnly = true)
    public List<ChavePixResponseDTO> getChavesPixDoUsuario(Usuario usuario)
    {
        return chavePixRepository.findByUsuarioIdUsuarios(usuario).stream().map(this::toResponseDto).collect(Collectors.toList());
    }

    @Transactional
    public ChavePixResponseDTO addChavePix(Usuario usuario, ChavePixCreateRequestDTO dto)
    {
        TipoChavePix tipoChave = tipoChavePixRepository.findById(dto.getTipoChavePixId())
                .orElseThrow(() -> new EntityNotFoundException("Tipo de chave PIX não encontrado."));

        ChavePixUsuario novaChave = new ChavePixUsuario();
        novaChave.setUsuarioIdUsuarios(usuario);
        novaChave.setTipoChavePixIdTiposchavepix(tipoChave);
        novaChave.setValorChave(dto.getValorChave());

        boolean isFirstKey = chavePixRepository.findByUsuarioIdUsuarios(usuario).isEmpty();
        
        novaChave.setAtivaParaDesembolso(isFirstKey);
        novaChave.setVerificada(false);

        ChavePixUsuario chaveSalva = chavePixRepository.save(novaChave);
        
        return toResponseDto(chaveSalva);
    }
    
    @Transactional
    public void setChaveAtiva(Usuario usuario, UUID chaveId)
    {
        ChavePixUsuario chave = chavePixRepository.findById(chaveId).orElseThrow(() -> new EntityNotFoundException("Chave PIX não encontrada"));
        
        if (!chave.getUsuarioIdUsuarios().getId().equals(usuario.getId()))
        {
            throw new org.springframework.security.access.AccessDeniedException("Acesso negado.");
        }
        
        desativarOutrasChaves(usuario, chaveId);
        
        chave.setAtivaParaDesembolso(true);
        chavePixRepository.save(chave);
    }

    @Transactional
    public void deleteChavePix(Usuario usuario, UUID chaveId)
    {
        ChavePixUsuario chave = chavePixRepository.findById(chaveId).orElseThrow(() -> new EntityNotFoundException("Chave PIX não encontrada."));

        if(!chave.getUsuarioIdUsuarios().getId().equals(usuario.getId()))
        {
            throw new org.springframework.security.access.AccessDeniedException("Acesso negado.");
        }
        
        if (chave.getAtivaParaDesembolso())
        {
            throw new IllegalStateException("Não é possível excluir a chave PIX ativa para desembolso.");
        }

        chavePixRepository.delete(chave);
    }
    
    private void desativarOutrasChaves(Usuario usuario, UUID chaveAtivaId)
    {
        List<ChavePixUsuario> chavesAtivas = chavePixRepository.findByUsuarioIdUsuarios(usuario).stream().filter(ChavePixUsuario::getAtivaParaDesembolso).toList();

        for (ChavePixUsuario chave : chavesAtivas)
        {
            if(!chave.getId().equals(chaveAtivaId))
            {
                chave.setAtivaParaDesembolso(false);
                chavePixRepository.save(chave);
            }
        }
    }

    private ChavePixResponseDTO toResponseDto(ChavePixUsuario chave)
    {
        String valorMascarado = mascararValorChave(chave.getValorChave(), chave.getTipoChavePixIdTiposchavepix().getNomeTipo());

        return new ChavePixResponseDTO(
            chave.getId(),
            chave.getTipoChavePixIdTiposchavepix().getNomeTipo(),
            valorMascarado,
            chave.getAtivaParaDesembolso(),
            chave.getVerificada(),
            chave.getDataCadastro()
        );
    }

    private String mascararValorChave(String valor, String tipo) 
    {
        if (valor == null || valor.isEmpty()) {
            return "";
        }
        return switch (tipo)
        {
            case "CPF" -> valor.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "***.$2.$3-**");
            case "E-mail" -> valor.replaceAll("(?<=.).(?=.*@)", "*");
            case "Telefone" -> valor.replaceAll("(\\d{2})(\\d{5})(\\d{4})", "($1) *****-$3");
            default -> valor;
        };
    }
}