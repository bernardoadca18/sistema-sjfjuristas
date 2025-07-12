package com.sjfjuristas.plataforma.backend.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sjfjuristas.plataforma.backend.domain.PropostaHistorico;
import com.sjfjuristas.plataforma.backend.dto.PropostasEmprestimo.PropostaHistoricoResponseDTO;
import com.sjfjuristas.plataforma.backend.repository.PropostaHistoricoRepository;

import jakarta.transaction.Transactional;

@Service
public class PropostaHistoricoService
{
    @Autowired
    PropostaHistoricoRepository propostaHistoricoRepository;

    @Transactional
    public Page<PropostaHistoricoResponseDTO> findByPropostaId(UUID propostaId, Pageable pageable)
    {
        Page<PropostaHistorico> pagePropostas = propostaHistoricoRepository.findByPropostaIdOrderByDataAlteracaoDesc(propostaId, pageable);

        return pagePropostas.map(PropostaHistoricoResponseDTO::new);
    }
}
