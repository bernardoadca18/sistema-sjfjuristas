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

import com.sjfjuristas.plataforma.backend.dto.CRUD.TipoDocumentoRequestDTO;
import com.sjfjuristas.plataforma.backend.dto.CRUD.TipoDocumentoResponseDTO;
import com.sjfjuristas.plataforma.backend.service.CRUD.TipoDocumentoCRUDService;

@RestController
@RequestMapping("/api/admin/tipo-documento")
public class TipoDocumentoCRUDController
{
    @Autowired
    private TipoDocumentoCRUDService tipoDocumentoCRUDService;

    @GetMapping
    public ResponseEntity<Page<TipoDocumentoResponseDTO>> getAllTiposDocumento(@PageableDefault(page = 0, size = 10, sort = "nomeDocumento") Pageable pageable)
    {
        Page<TipoDocumentoResponseDTO> response = tipoDocumentoCRUDService.getAllTiposDocumento(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<TipoDocumentoResponseDTO>> getAllTiposDocumento()
    {
        List<TipoDocumentoResponseDTO> response = tipoDocumentoCRUDService.getAllTiposDocumento();
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TipoDocumentoResponseDTO> getTipoDocumentoById(@PathVariable UUID id)
    {
        TipoDocumentoResponseDTO response = tipoDocumentoCRUDService.getTipoDocumentoById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{nomeDocumento}")
    public ResponseEntity<TipoDocumentoResponseDTO> getTipoDocumentoByNomeDocumento(@PathVariable String nomeDocumento)
    {
        TipoDocumentoResponseDTO response = tipoDocumentoCRUDService.getTipoDocumentoByTipoDocumento(nomeDocumento);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<TipoDocumentoResponseDTO> createTipoDocumento(@Valid @RequestBody TipoDocumentoRequestDTO dto)
    {
        TipoDocumentoResponseDTO response = tipoDocumentoCRUDService.createTipoDocumento(dto);
        return ResponseEntity.ok(response);        
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoDocumentoResponseDTO> updateTipoDocumento(@PathVariable UUID id, @Valid @RequestBody TipoDocumentoRequestDTO dto)
    {
        TipoDocumentoResponseDTO response = tipoDocumentoCRUDService.updateTipoDocumento(id, dto);
        return ResponseEntity.ok(response);        
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TipoDocumentoResponseDTO> deleteTipoDocumento(@PathVariable UUID id)
    {
        tipoDocumentoCRUDService.deleteTipoDocumento(id);
        return ResponseEntity.noContent().build();
    }
}