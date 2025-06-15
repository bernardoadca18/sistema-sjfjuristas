package com.sjfjuristas.plataforma.backend.controller;

import com.sjfjuristas.plataforma.backend.dto.PropostasEmprestimo.PropostaRequestDTO;
import com.sjfjuristas.plataforma.backend.dto.PropostasEmprestimo.PropostaResponseDTO;
import com.sjfjuristas.plataforma.backend.service.PropostaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/propostas")
@CrossOrigin(origins = "*")
public class PropostaController {

    @Autowired
    private PropostaService propostaService;

    @PostMapping
    public ResponseEntity<PropostaResponseDTO> receberProposta(
            @Valid @RequestBody PropostaRequestDTO propostaDTO,
            HttpServletRequest request
    ) {
        PropostaResponseDTO propostaCriadaDTO = propostaService.criarProposta(propostaDTO, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(propostaCriadaDTO);
    }
}