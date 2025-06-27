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

import com.sjfjuristas.plataforma.backend.dto.CRUD.StatusEmprestimoRequestDTO;
import com.sjfjuristas.plataforma.backend.dto.CRUD.StatusEmprestimoResponseDTO;
import com.sjfjuristas.plataforma.backend.service.CRUD.StatusEmprestimoCRUDService;

@RestController
@RequestMapping("/api/admin/status-emprestimo")
public class StatusEmprestimoCRUDController
{
    @Autowired
    private StatusEmprestimoCRUDService statusEmprestimoCRUDService;

    @GetMapping
    public ResponseEntity<Page<StatusEmprestimoResponseDTO>> getAllStatusEmprestimo(@PageableDefault(page = 0, size = 10, sort = "nomeStatus") Pageable pageable)
    {
        Page<StatusEmprestimoResponseDTO> response = statusEmprestimoCRUDService.getAllStatusEmprestimos(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/non-paged")
    public ResponseEntity<List<StatusEmprestimoResponseDTO>> getAllStatusEmprestimoNonPaged()
    {
        List<StatusEmprestimoResponseDTO> response = statusEmprestimoCRUDService.getAllStatusEmprestimos();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StatusEmprestimoResponseDTO> getStatusEmprestimoById(@PathVariable UUID id)
    {
        StatusEmprestimoResponseDTO response = statusEmprestimoCRUDService.getStatusEmprestimoById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/nome-status/{nomeStatus}")
    public ResponseEntity<StatusEmprestimoResponseDTO> getStatusEmprestimoByNomeStatus(@PathVariable String nomeStatus)
    {
        StatusEmprestimoResponseDTO response = statusEmprestimoCRUDService.getStatusEmprestimoByNomeStatus(nomeStatus);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<StatusEmprestimoResponseDTO> createStatusEmprestimo(@Valid @RequestBody StatusEmprestimoRequestDTO dto)
    {
        StatusEmprestimoResponseDTO response = statusEmprestimoCRUDService.createStatusEmprestimo(dto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StatusEmprestimoResponseDTO> updateStatusEmprestimo(@PathVariable UUID id, @Valid @RequestBody StatusEmprestimoRequestDTO dto)
    {
        StatusEmprestimoResponseDTO response = statusEmprestimoCRUDService.updateStatusEmprestimo(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StatusEmprestimoResponseDTO> deleteStatusEmprestimo(@PathVariable UUID id)
    {
        statusEmprestimoCRUDService.deleteStatusEmprestimo(id);
        return ResponseEntity.noContent().build();
    }
}