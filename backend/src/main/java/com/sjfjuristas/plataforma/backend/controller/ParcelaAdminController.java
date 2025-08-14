package com.sjfjuristas.plataforma.backend.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sjfjuristas.plataforma.backend.dto.ComprovantesPagamento.ComprovanteResponseDTO;
import com.sjfjuristas.plataforma.backend.service.DocumentoPropostaService;

@RestController
@RequestMapping("/api/admin/parcelas")
public class ParcelaAdminController
{
    @Autowired
    private DocumentoPropostaService documentoPropostaService;

    @GetMapping("/{parcelaId}/comprovantes")
    public ResponseEntity<List<ComprovanteResponseDTO>> getComprovantesDaParcela(@PathVariable UUID parcelaId)
    {
        List<ComprovanteResponseDTO> comprovantes = documentoPropostaService.getComprovantesByParcelaId(parcelaId);
        return ResponseEntity.ok(comprovantes);
    }
}