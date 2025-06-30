package com.sjfjuristas.plataforma.backend.service.CRUD;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sjfjuristas.plataforma.backend.dto.CRUD.OcupacaoRequestDTO;
import com.sjfjuristas.plataforma.backend.dto.CRUD.OcupacaoResponseDTO;
import com.sjfjuristas.plataforma.backend.repository.OcupacaoRepository;
import com.sjfjuristas.plataforma.backend.domain.Ocupacao;

@Service
public class OcupacaoCRUDService
{
    @Autowired
    private OcupacaoRepository ocupacaoRepository;

    @Transactional(readOnly = true)
    public Page<OcupacaoResponseDTO> getAllOcupacoes(Pageable pageable)
    {
        Page<Ocupacao> ocupacoes = ocupacaoRepository.findAll(pageable);
        return ocupacoes.map(OcupacaoResponseDTO::new);
    }
    
    @Transactional(readOnly = true)
    public OcupacaoResponseDTO getOcupacaoById(UUID id)
    {
        Ocupacao ocupacao = ocupacaoRepository.findById(id).orElseThrow(() -> new RuntimeException("Ocupação não encontrada"));
        return new OcupacaoResponseDTO(ocupacao);
    }

    @Transactional
    public OcupacaoResponseDTO createOcupacao(OcupacaoRequestDTO dto)
    {
        Ocupacao ocupacao = new Ocupacao();
        
        ocupacao.setNomeOcupacao(dto.getNomeOcupacao());
        ocupacao.setAtivo(dto.isAtivo());

        ocupacao = ocupacaoRepository.save(ocupacao);

        return new OcupacaoResponseDTO(ocupacao);
    }

    @Transactional
    public OcupacaoResponseDTO updateOcupacao(UUID id, OcupacaoRequestDTO dto)
    {
        Ocupacao ocupacao = ocupacaoRepository.findById(id).orElseThrow(() -> new RuntimeException("Ocupação não encontrada"));
        
        ocupacao.setNomeOcupacao(dto.getNomeOcupacao());
        ocupacao.setAtivo(dto.isAtivo());

        ocupacao = ocupacaoRepository.save(ocupacao);

        return new OcupacaoResponseDTO(ocupacao);
    }

    @Transactional
    public void deleteOcupacao(UUID id)
    {
        Ocupacao ocupacao = ocupacaoRepository.findById(id).orElseThrow(() -> new RuntimeException("Ocupação não encontrada"));
        ocupacaoRepository.delete(ocupacao);
    }

    @Transactional(readOnly = true)
    public List<OcupacaoResponseDTO> getAllAtivas(boolean ativo)
    {
        List<Ocupacao> ocupacoes = ocupacaoRepository.findByAtivoTrueOrderByNomeOcupacaoAsc();
        return ocupacoes.stream().map(OcupacaoResponseDTO::new).toList();
    }
    
    @Transactional(readOnly = true)
    public List<OcupacaoResponseDTO> getAllOcupacoes()
    {
        List<Ocupacao> ocupacoes = ocupacaoRepository.findAll();
        return ocupacoes.stream().map(OcupacaoResponseDTO::new).toList();
    }
}