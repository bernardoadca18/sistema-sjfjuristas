package com.sjfjuristas.plataforma.backend.service;

import com.sjfjuristas.plataforma.backend.domain.ChavePixUsuario;
import com.sjfjuristas.plataforma.backend.domain.TipoChavePix;
import com.sjfjuristas.plataforma.backend.domain.Usuario;
import com.sjfjuristas.plataforma.backend.dto.ChavesPixUsuario.ChavePixCreateRequestDTO;
import com.sjfjuristas.plataforma.backend.dto.ChavesPixUsuario.ChavePixResponseDTO;
import com.sjfjuristas.plataforma.backend.repository.ChavePixUsuarioRepository;
import com.sjfjuristas.plataforma.backend.repository.TipoChavePixRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ChavePixUsuarioService {

    @Autowired
    private ChavePixUsuarioRepository chavePixRepository;

    @Autowired
    private TipoChavePixRepository tipoChavePixRepository;

    @Transactional(readOnly = true)
    public List<ChavePixResponseDTO> getChavesPixDoUsuario(Usuario usuario) {
        return chavePixRepository.findByUsuarioIdUsuarios(usuario).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ChavePixResponseDTO addChavePix(Usuario usuario, ChavePixCreateRequestDTO dto) {
        TipoChavePix tipoChave = tipoChavePixRepository.findById(dto.getTipoChavePixId())
                .orElseThrow(() -> new EntityNotFoundException("Tipo de chave PIX não encontrado."));

        ChavePixUsuario novaChave = new ChavePixUsuario();
        novaChave.setUsuarioIdUsuarios(usuario);
        novaChave.setTipoChavePixIdTiposchavepix(tipoChave);
        novaChave.setValorChave(dto.getValorChave());
        novaChave.setAtivaParaDesembolso(true);
        
        desativarOutrasChaves(usuario);

        ChavePixUsuario chaveSalva = chavePixRepository.save(novaChave);
        return toResponseDto(chaveSalva);
    }
    
    @Transactional
    public void setChaveAtiva(Usuario usuario, UUID chaveId) {
        desativarOutrasChaves(usuario);
        ChavePixUsuario chave = chavePixRepository.findById(chaveId)
            .orElseThrow(() -> new EntityNotFoundException("Chave PIX não encontrada"));
        
        if (!chave.getUsuarioIdUsuarios().getId().equals(usuario.getId())) {
            throw new org.springframework.security.access.AccessDeniedException("Acesso negado.");
        }
        chave.setAtivaParaDesembolso(true);
        chavePixRepository.save(chave);
    }
    
    private void desativarOutrasChaves(Usuario usuario) {
        List<ChavePixUsuario> chavesAtivas = chavePixRepository.findByUsuarioIdUsuarios(usuario).stream()
            .filter(ChavePixUsuario::getAtivaParaDesembolso)
            .toList();

        for (ChavePixUsuario chave : chavesAtivas) {
            chave.setAtivaParaDesembolso(false);
            chavePixRepository.save(chave);
        }
    }

    private ChavePixResponseDTO toResponseDto(ChavePixUsuario chave) {
        return new ChavePixResponseDTO(
            chave.getId(),
            chave.getTipoChavePixIdTiposchavepix().getNomeTipo(),
            chave.getValorChave(), // TODO: Mascarar em produção
            chave.getAtivaParaDesembolso(),
            chave.getVerificada(),
            chave.getDataCadastro()
        );
    }
}