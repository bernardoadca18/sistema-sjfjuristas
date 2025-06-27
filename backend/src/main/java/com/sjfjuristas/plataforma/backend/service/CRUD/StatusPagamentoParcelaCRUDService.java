package com.sjfjuristas.plataforma.backend.service.CRUD;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sjfjuristas.plataforma.backend.domain.StatusPagamentoParcela;
import com.sjfjuristas.plataforma.backend.dto.CRUD.StatusPagamentoParcelaResponseDTO;
import com.sjfjuristas.plataforma.backend.dto.CRUD.StatusPagamentoParcelaRequestDTO;
import com.sjfjuristas.plataforma.backend.repository.StatusPagamentoParcelaRepository;

@Service
public class StatusPagamentoParcelaCRUDService
{
    @Autowired
    private StatusPagamentoParcelaRepository statusPagamentoParcelaRepository;

    @Transactional(readOnly = true)
    public Page<StatusPagamentoParcelaResponseDTO> getAllStatusPagamentoParcela(Pageable pageable)
    {
        Page<StatusPagamentoParcela> pagamentosParcela = statusPagamentoParcelaRepository.findAll(pageable);

        return pagamentosParcela.map(StatusPagamentoParcelaResponseDTO::new);
    }

    @Transactional(readOnly = true)
    public List<StatusPagamentoParcelaResponseDTO> getAllStatusPagamentoParcela()
    {
        List<StatusPagamentoParcela> pagamentosParcela = statusPagamentoParcelaRepository.findAll();

        return pagamentosParcela.stream().map(StatusPagamentoParcelaResponseDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public StatusPagamentoParcelaResponseDTO getStatusPagamentoParcelaById(UUID id)
    {
        StatusPagamentoParcela statusPagamentoParcela = statusPagamentoParcelaRepository.findById(id).orElseThrow(() -> new RuntimeException("Não foi encontrado nenhum valor com o id informado: " + id));

        return new StatusPagamentoParcelaResponseDTO(statusPagamentoParcela);
    }

    @Transactional(readOnly = true)
    public StatusPagamentoParcelaResponseDTO getStatusPagamentoParcelaByNomeStatus(String nomeStatus)
    {
        StatusPagamentoParcela statusPagamentoParcela = statusPagamentoParcelaRepository.findByNomeStatus(nomeStatus).orElseThrow(() -> new RuntimeException("Não foi encontrado nenhum valor com o nome de status informado: " + nomeStatus));

        return new StatusPagamentoParcelaResponseDTO(statusPagamentoParcela);
    }

    @Transactional
    public StatusPagamentoParcelaResponseDTO createStatusPagamentoParcela(StatusPagamentoParcelaRequestDTO dto)
    {
        StatusPagamentoParcela statusPagamentoParcela = new StatusPagamentoParcela();

        statusPagamentoParcela.setNomeStatus(dto.getNomeStatus());
        statusPagamentoParcela = statusPagamentoParcelaRepository.save(statusPagamentoParcela);

        return new StatusPagamentoParcelaResponseDTO(statusPagamentoParcela);
    }

    @Transactional
    public StatusPagamentoParcelaResponseDTO updateStatusPagamentoParcela(UUID id, StatusPagamentoParcelaRequestDTO dto)
    {
        StatusPagamentoParcela statusPagamentoParcela = statusPagamentoParcelaRepository.findById(id).orElseThrow(() -> new RuntimeException("Não foi encontrado nenhum valor com o id informado: " + id));

        statusPagamentoParcela.setNomeStatus(dto.getNomeStatus());
        statusPagamentoParcela = statusPagamentoParcelaRepository.save(statusPagamentoParcela);

        return new StatusPagamentoParcelaResponseDTO(statusPagamentoParcela);
    }

    @Transactional
    public void deleteStatusPagamentoParcela(UUID id)
    {
        StatusPagamentoParcela statusPagamentoParcela = statusPagamentoParcelaRepository.findById(id).orElseThrow(() -> new RuntimeException("Não foi encontrado nenhum valor com o id informado: " + id));

        statusPagamentoParcelaRepository.delete(statusPagamentoParcela);
    }
}