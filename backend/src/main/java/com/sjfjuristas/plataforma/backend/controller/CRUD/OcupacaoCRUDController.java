package com.sjfjuristas.plataforma.backend.controller.CRUD;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

import com.sjfjuristas.plataforma.backend.dto.CRUD.OcupacaoRequestDTO;
import com.sjfjuristas.plataforma.backend.dto.CRUD.OcupacaoResponseDTO;
import com.sjfjuristas.plataforma.backend.service.CRUD.OcupacaoCRUDService;

@RestController
@RequestMapping("/api/admin/ocupacoes")
public class OcupacaoCRUDController
{
    @Autowired
    private OcupacaoCRUDService ocupacaoCRUDService;

    @GetMapping
    public ResponseEntity<Page<OcupacaoResponseDTO>> getAllOcupacoes(@PageableDefault(page = 0, size = 10, sort = "nomeOcupacao") Pageable pageable)
    {
        Page<OcupacaoResponseDTO> response = ocupacaoCRUDService.getAllOcupacoes(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/non-paged")
    public ResponseEntity<List<OcupacaoResponseDTO>> getAllOcupacoesNonPaged()
    {
        List<OcupacaoResponseDTO> response = ocupacaoCRUDService.getAllOcupacoes();
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<OcupacaoResponseDTO> getOcupacaoById(@PathVariable UUID id)
    {
        OcupacaoResponseDTO response = ocupacaoCRUDService.getOcupacaoById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping(params = "ativa")
    public ResponseEntity<List<OcupacaoResponseDTO>> getOcupacoesAtivas(@RequestParam boolean ativa)
    {
        List<OcupacaoResponseDTO> ocupacoesAtivas = ocupacaoCRUDService.getAllAtivas(ativa);
        return ResponseEntity.ok(ocupacoesAtivas);
    }

    @PostMapping
    public ResponseEntity<OcupacaoResponseDTO> createOcupacao(@Valid @RequestBody OcupacaoRequestDTO request)
    {
        OcupacaoResponseDTO response = ocupacaoCRUDService.createOcupacao(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OcupacaoResponseDTO> updateOcupacao(@PathVariable UUID id, @Valid @RequestBody OcupacaoRequestDTO request)
    {
        OcupacaoResponseDTO response = ocupacaoCRUDService.updateOcupacao(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<OcupacaoResponseDTO> deleteOcupacao(@PathVariable UUID id)
    {
        ocupacaoCRUDService.deleteOcupacao(id);
        return ResponseEntity.noContent().build();
    }
}