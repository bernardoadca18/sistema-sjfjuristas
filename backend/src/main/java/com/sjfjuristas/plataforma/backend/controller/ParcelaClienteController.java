package com.sjfjuristas.plataforma.backend.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sjfjuristas.plataforma.backend.domain.Usuario;
import com.sjfjuristas.plataforma.backend.dto.ParcelaEmprestimo.ParcelaEmprestimoResponseDTO;
import com.sjfjuristas.plataforma.backend.service.ParcelaEmprestimoService;


@RestController
@RequestMapping("/api/cliente/parcelas")
public class ParcelaClienteController
{
    @Autowired
    private ParcelaEmprestimoService parcelaService;

    @GetMapping("/{id}")
    public ResponseEntity<ParcelaEmprestimoResponseDTO> getParcelaById(@PathVariable UUID id, @AuthenticationPrincipal Usuario usuarioLogado)
    {
        ParcelaEmprestimoResponseDTO parcela = parcelaService.getParcelaDTOByIdAndUsuario(id, usuarioLogado.getId());
        return ResponseEntity.ok(parcela);
    }
}