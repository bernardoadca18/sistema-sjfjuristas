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

import com.sjfjuristas.plataforma.backend.dto.CRUD.TipoChavePixRequestDTO;
import com.sjfjuristas.plataforma.backend.dto.CRUD.TipoChavePixResponseDTO;
import com.sjfjuristas.plataforma.backend.service.CRUD.TipoChavePixCRUDService;

@RestController
@RequestMapping("/api/admin/tipo-chave-pix")
public class TipoChavePixCRUDController
{
    @Autowired
    private TipoChavePixCRUDService tipoChavePixCRUDService;

    @GetMapping 
    public ResponseEntity<Page<TipoChavePixResponseDTO>> getAllTiposChavePix(@PageableDefault(page = 0, size = 10, sort = "nomeTipo") Pageable pageable)
    {
        Page<TipoChavePixResponseDTO> response = tipoChavePixCRUDService.getAllTiposChavePix(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping ("/non-paged")
    public ResponseEntity<List<TipoChavePixResponseDTO>> getAllTiposChavePixNonPaged()
    {
        List<TipoChavePixResponseDTO> response = tipoChavePixCRUDService.getAllTiposChavePix();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoChavePixResponseDTO> getTipoChavePixById(@PathVariable UUID id)
    {
        TipoChavePixResponseDTO response = tipoChavePixCRUDService.getTipoChavePixById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/nome-tipo/{nomeTipo}")
    public ResponseEntity<TipoChavePixResponseDTO> getTipoChavePixByNomeTipo(@PathVariable String nomeTipo)
    {
        TipoChavePixResponseDTO response = tipoChavePixCRUDService.getTipoChavePixByNomeTipo(nomeTipo);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<TipoChavePixResponseDTO> createTipoChavePix(@Valid @RequestBody TipoChavePixRequestDTO dto)
    {
        TipoChavePixResponseDTO response = tipoChavePixCRUDService.createTipoChavePix(dto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoChavePixResponseDTO> updateTipoChavePix(@PathVariable UUID id, @Valid @RequestBody TipoChavePixRequestDTO dto)
    {
        TipoChavePixResponseDTO response = tipoChavePixCRUDService.updateTipoChavePix(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TipoChavePixResponseDTO> deleteTipoChavePix(@PathVariable UUID id)
    {
        tipoChavePixCRUDService.deleteTipoChavePix(id);
        return ResponseEntity.noContent().build();
    }
}