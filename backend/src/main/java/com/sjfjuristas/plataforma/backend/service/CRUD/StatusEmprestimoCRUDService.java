package com.sjfjuristas.plataforma.backend.service.CRUD;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sjfjuristas.plataforma.backend.domain.StatusEmprestimo;
import com.sjfjuristas.plataforma.backend.dto.CRUD.StatusEmprestimoRequestDTO;
import com.sjfjuristas.plataforma.backend.dto.CRUD.StatusEmprestimoResponseDTO;
import com.sjfjuristas.plataforma.backend.repository.StatusEmprestimoRepository;

@Service
public class StatusEmprestimoCRUDService
{
    @Autowired
    private StatusEmprestimoRepository statusEmprestimoRepository;

    @Transactional(readOnly = true)
    public Page<StatusEmprestimoResponseDTO> getAllStatusEmprestimos(Pageable pageable)
    {
        Page<StatusEmprestimo> statusEmprestimos = statusEmprestimoRepository.findAll(pageable);
        return statusEmprestimos.map(StatusEmprestimoResponseDTO::new);
    }

    @Transactional(readOnly = true)
    public List<StatusEmprestimoResponseDTO> getAllStatusEmprestimos()
    {
        List<StatusEmprestimo> statusEmprestimos = statusEmprestimoRepository.findAll();
        return statusEmprestimos.stream().map(StatusEmprestimoResponseDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public StatusEmprestimoResponseDTO getStatusEmprestimoById(UUID id)
    {
        StatusEmprestimo statusEmprestimo = statusEmprestimoRepository.findById(id).orElseThrow(() -> new RuntimeException("Status de empréstimo não encontrado"));
        return new StatusEmprestimoResponseDTO(statusEmprestimo);
    }

    @Transactional(readOnly = true)
    public StatusEmprestimoResponseDTO getStatusEmprestimoByNomeStatus(String nomeStatus)
    {
        StatusEmprestimo statusEmprestimo = statusEmprestimoRepository.findByNomeStatus(nomeStatus).orElseThrow(() -> new RuntimeException("Status de empréstimo com nome '" + nomeStatus + "' não encontrado"));
        return new StatusEmprestimoResponseDTO(statusEmprestimo);
    }

    @Transactional
    public StatusEmprestimoResponseDTO createStatusEmprestimo(StatusEmprestimoRequestDTO dto)
    {
        StatusEmprestimo statusEmprestimo = new StatusEmprestimo();

        statusEmprestimo.setNomeStatus(dto.getNomeStatus());

        statusEmprestimo = statusEmprestimoRepository.save(statusEmprestimo);

        return new StatusEmprestimoResponseDTO(statusEmprestimo);
    }

    @Transactional
    public StatusEmprestimoResponseDTO updateStatusEmprestimo(UUID id, StatusEmprestimoRequestDTO dto)
    {
        StatusEmprestimo statusEmprestimo = new StatusEmprestimo();

        statusEmprestimo.setNomeStatus(dto.getNomeStatus());
        statusEmprestimo = statusEmprestimoRepository.save(statusEmprestimo);

        return new StatusEmprestimoResponseDTO(statusEmprestimo);
    }

    @Transactional
    public void deleteStatusEmprestimo(UUID id)
    {
        StatusEmprestimo statusEmprestimo = statusEmprestimoRepository.findById(id).orElseThrow(() -> new RuntimeException("Status de empréstimo com id '" + id + "' não encontrado"));
        statusEmprestimoRepository.delete(statusEmprestimo);
    }
    
}