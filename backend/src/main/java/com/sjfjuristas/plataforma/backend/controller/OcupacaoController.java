package com.sjfjuristas.plataforma.backend.controller;

import com.sjfjuristas.plataforma.backend.dto.Ocupacao.OcupacaoResponseDTO;
import com.sjfjuristas.plataforma.backend.service.OcupacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ocupacoes")
@CrossOrigin(origins = "*")
public class OcupacaoController
{

    @Autowired
    private OcupacaoService ocupacaoService;

    @GetMapping
    public ResponseEntity<List<OcupacaoResponseDTO>> listarOcupacoes()
    {
        List<OcupacaoResponseDTO> lista = ocupacaoService.listarOcupacoesAtivas().stream()
        .map(ocupacao -> new OcupacaoResponseDTO(ocupacao.getId(), ocupacao.getNomeOcupacao()))
        .collect(Collectors.toList());
        
        return ResponseEntity.ok(lista);
    }
}