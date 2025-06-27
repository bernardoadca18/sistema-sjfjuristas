package com.sjfjuristas.plataforma.backend.controller.CRUD;

import java.util.List;
import java.util.UUID;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sjfjuristas.plataforma.backend.dto.CRUD.StatusPropostaRequestDTO;
import com.sjfjuristas.plataforma.backend.dto.CRUD.StatusPropostaResponseDTO;
import com.sjfjuristas.plataforma.backend.service.CRUD.StatusPropostaCRUDService;

@RestController
@RequestMapping("/api/admin/status-proposta")
public class StatusPropostaCRUDController
{
    @Autowired
    private StatusPropostaCRUDService statusPropostaCRUDService;

    @GetMapping
    public ResponseEntity<Page<StatusPropostaResponseDTO>> getAllStatusProposta(@PageableDefault(page = 0, size = 10, sort = "nomeStatus") Pageable pageable)
    {
        Page<StatusPropostaResponseDTO> response = statusPropostaCRUDService.getAllStatusProposta(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<StatusPropostaResponseDTO>> getAllStatusProposta()
    {
        List<StatusPropostaResponseDTO> response = statusPropostaCRUDService.getAllStatusProposta();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StatusPropostaResponseDTO> getStatusPropostaById(@PathVariable UUID id)
    {
        StatusPropostaResponseDTO response = statusPropostaCRUDService.getStatusPropostaById(id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{nomeStatus}")
    public ResponseEntity<StatusPropostaResponseDTO> getStatusPropostaByNomeStatus(@PathVariable String nomeStatus)
    {
        StatusPropostaResponseDTO response = statusPropostaCRUDService.getStatusPropostaByNomeStatus(nomeStatus);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<StatusPropostaResponseDTO> createStatusProposta(@Valid @RequestBody StatusPropostaRequestDTO dto)
    {
        StatusPropostaResponseDTO response = statusPropostaCRUDService.createStatusProposta(dto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StatusPropostaResponseDTO> updateStatusProposta(@PathVariable UUID id, @Valid @RequestBody StatusPropostaRequestDTO dto)
    {
        StatusPropostaResponseDTO response = statusPropostaCRUDService.updateStatusProposta(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StatusPropostaResponseDTO> deleteStatusProposta(@PathVariable UUID id)
    {
        statusPropostaCRUDService.deleteStatusProposta(id);
        return ResponseEntity.noContent().build();
    }
}