package com.sjfjuristas.plataforma.backend.service.CRUD;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sjfjuristas.plataforma.backend.domain.StatusProposta;
import com.sjfjuristas.plataforma.backend.dto.CRUD.StatusPropostaRequestDTO;
import com.sjfjuristas.plataforma.backend.dto.CRUD.StatusPropostaResponseDTO;
import com.sjfjuristas.plataforma.backend.repository.StatusPropostaRepository;

@Service
public class StatusPropostaCRUDService
{
    @Autowired
    private StatusPropostaRepository statusPropostaRepository;

    @Transactional(readOnly = true)
    public Page<StatusPropostaResponseDTO> getAllStatusProposta(Pageable pageable)
    {
        Page<StatusProposta> statusPropostas = statusPropostaRepository.findAll(pageable);

        return statusPropostas.map(StatusPropostaResponseDTO::new);
    }

    @Transactional(readOnly = true)
    public List<StatusPropostaResponseDTO> getAllStatusProposta()
    {
        List<StatusProposta> statusPropostas = statusPropostaRepository.findAll();

        return statusPropostas.stream().map(StatusPropostaResponseDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public StatusPropostaResponseDTO getStatusPropostaById(UUID id)
    {
        StatusProposta statusProposta = statusPropostaRepository.findById(id).orElseThrow(() -> new RuntimeException("Não foi encontrado nenhum valor com o id informado: " + id));

        return new StatusPropostaResponseDTO(statusProposta);
    }

    @Transactional(readOnly = true)
    public StatusPropostaResponseDTO getStatusPropostaByNomeStatus(String nomeStatus)
    {
        StatusProposta statusProposta = statusPropostaRepository.findByNomeStatus(nomeStatus).orElseThrow(() -> new RuntimeException("Não foi encontrado nenhum valor com o nomeStatus informado: " + nomeStatus));

        return new StatusPropostaResponseDTO(statusProposta);
    }

    @Transactional
    public StatusPropostaResponseDTO createStatusProposta(StatusPropostaRequestDTO dto)
    {
        StatusProposta statusProposta = new StatusProposta();

        statusProposta.setNomeStatus(dto.getNomeStatus());
        statusProposta = statusPropostaRepository.save(statusProposta);

        return new StatusPropostaResponseDTO(statusProposta);
    }

    @Transactional
    public StatusPropostaResponseDTO updateStatusProposta(UUID id, StatusPropostaRequestDTO dto)
    {
        StatusProposta statusProposta = statusPropostaRepository.findById(id).orElseThrow(() -> new RuntimeException("Não foi encontrado nenhum valor com o id informado: " + id));

        statusProposta.setNomeStatus(dto.getNomeStatus());
        statusProposta = statusPropostaRepository.save(statusProposta);

        return new StatusPropostaResponseDTO(statusProposta);
    }

    @Transactional
    public void deleteStatusProposta(UUID id)
    {
        StatusProposta statusProposta = statusPropostaRepository.findById(id).orElseThrow(() -> new RuntimeException("Não foi encontrado nenhum valor com o id informado: " + id));

        statusPropostaRepository.delete(statusProposta);
    }
    
}