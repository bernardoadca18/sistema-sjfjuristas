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

import com.sjfjuristas.plataforma.backend.dto.CRUD.StatusPagamentoParcelaRequestDTO;
import com.sjfjuristas.plataforma.backend.dto.CRUD.StatusPagamentoParcelaResponseDTO;
import com.sjfjuristas.plataforma.backend.service.CRUD.StatusPagamentoParcelaCRUDService;

@RestController
@RequestMapping("/api/admin/status-pagamento-parcela")
public class StatusPagamentoParcelaCRUDController
{
    @Autowired
    private StatusPagamentoParcelaCRUDService statusPagamentoParcelaCRUDService;

    @GetMapping
    public ResponseEntity<Page<StatusPagamentoParcelaResponseDTO>> getAllStatusPagamentoParcela(@PageableDefault(page = 0, size = 10, sort = "nomeStatus") Pageable pageable)
    {
        Page<StatusPagamentoParcelaResponseDTO> response = statusPagamentoParcelaCRUDService.getAllStatusPagamentoParcela(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/non-paged")
    public ResponseEntity<List<StatusPagamentoParcelaResponseDTO>> getAllStatusPagamentoParcelaNonPaged()
    {
        List<StatusPagamentoParcelaResponseDTO> response = statusPagamentoParcelaCRUDService.getAllStatusPagamentoParcela();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StatusPagamentoParcelaResponseDTO> getStatusPagamentoParcelaById(@PathVariable UUID id)
    {
        StatusPagamentoParcelaResponseDTO response = statusPagamentoParcelaCRUDService.getStatusPagamentoParcelaById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/nome-status/{nomeStatus}")
    public ResponseEntity<StatusPagamentoParcelaResponseDTO> getStatusPagamentoParcelaByNomeStatus(@PathVariable String nomeStatus)
    {
        StatusPagamentoParcelaResponseDTO response = statusPagamentoParcelaCRUDService.getStatusPagamentoParcelaByNomeStatus(nomeStatus);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<StatusPagamentoParcelaResponseDTO> createStatusPagamentoParcela(@Valid @RequestBody StatusPagamentoParcelaRequestDTO dto)
    {
        StatusPagamentoParcelaResponseDTO response = statusPagamentoParcelaCRUDService.createStatusPagamentoParcela(dto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StatusPagamentoParcelaResponseDTO> updateStatusPagamentoParcela(@PathVariable UUID id, @Valid @RequestBody StatusPagamentoParcelaRequestDTO dto)
    {
        StatusPagamentoParcelaResponseDTO response = statusPagamentoParcelaCRUDService.updateStatusPagamentoParcela(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StatusPagamentoParcelaResponseDTO> deletetatusPagamentoParcela(@PathVariable UUID id)
    {
        statusPagamentoParcelaCRUDService.deleteStatusPagamentoParcela(id);
        return ResponseEntity.noContent().build();
    }
}