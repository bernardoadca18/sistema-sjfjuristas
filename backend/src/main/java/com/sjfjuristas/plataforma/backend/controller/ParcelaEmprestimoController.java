package com.sjfjuristas.plataforma.backend.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sjfjuristas.plataforma.backend.domain.Emprestimo;
import com.sjfjuristas.plataforma.backend.dto.ParcelaEmprestimo.ParcelaEmprestimoResponseDTO;
import com.sjfjuristas.plataforma.backend.repository.EmprestimoRepository;
import com.sjfjuristas.plataforma.backend.service.ParcelaEmprestimoService;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/cliente/emprestimos")
public class ParcelaEmprestimoController
{
    @Autowired
    private ParcelaEmprestimoService parcelaEmprestimoService;

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    @GetMapping("/{emprestimoId}/parcelas")
    public ResponseEntity<Page<ParcelaEmprestimoResponseDTO>> getMinhasParcelas(@PathVariable UUID emprestimoId, Pageable pageable)
    {
        validarPropriedadeEmprestimo(emprestimoId);
        Page<ParcelaEmprestimoResponseDTO> parcelas = parcelaEmprestimoService.getParcelasByEmprestimoId(emprestimoId, pageable);
        return ResponseEntity.ok(parcelas);
    }

    @GetMapping("/{emprestimoId}/proxima-parcela")
    public ResponseEntity<ParcelaEmprestimoResponseDTO> getProximaParcelaPagar(@PathVariable UUID emprestimoId)
    {
        validarPropriedadeEmprestimo(emprestimoId);
        ParcelaEmprestimoResponseDTO proximaParcela = parcelaEmprestimoService.getProximaParcela(emprestimoId);
        return ResponseEntity.ok(proximaParcela);
    }

    private void validarPropriedadeEmprestimo(UUID emprestimoId)
    {
        String emailUsuarioLogado = SecurityContextHolder.getContext().getAuthentication().getName();

        Emprestimo emprestimo = emprestimoRepository.findById(emprestimoId).orElseThrow(() -> new EntityNotFoundException("Empréstimo não encontrado com o ID: " + emprestimoId));

        if (!emprestimo.getUsuarioIdUsuarios().getEmail().equals(emailUsuarioLogado)) 
        {
            throw new AccessDeniedException("Acesso negado. Este empréstimo não pertence ao usuário autenticado.");
        }
    }
}
