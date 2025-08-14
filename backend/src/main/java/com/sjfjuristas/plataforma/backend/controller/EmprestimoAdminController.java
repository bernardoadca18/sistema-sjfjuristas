package com.sjfjuristas.plataforma.backend.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sjfjuristas.plataforma.backend.dto.Emprestimos.EmprestimoAdminResponseDTO;
import com.sjfjuristas.plataforma.backend.dto.ParcelaEmprestimo.ParcelaEmprestimoResponseDTO;
import com.sjfjuristas.plataforma.backend.service.EmprestimoService;
import com.sjfjuristas.plataforma.backend.service.ParcelaEmprestimoService;

@RestController
@RequestMapping("/api/admin/emprestimos")
public class EmprestimoAdminController
{
    @Autowired
    private EmprestimoService emprestimoService;

    @Autowired
    private ParcelaEmprestimoService parcelaEmprestimoService;

    @GetMapping
    public ResponseEntity<Page<EmprestimoAdminResponseDTO>> getEmprestimos( @RequestParam(required = false) UUID usuarioId, @PageableDefault(sort = "dataContratacao") Pageable pageable )
    {
        Page<EmprestimoAdminResponseDTO> emprestimos = emprestimoService.getAllEmprestimosAdmin(usuarioId, pageable);
        return ResponseEntity.ok(emprestimos);
    }

    @GetMapping("/{emprestimoId}/parcelas")
    public ResponseEntity<Page<ParcelaEmprestimoResponseDTO>> getParcelasDoEmprestimo(@PathVariable UUID emprestimoId, Pageable pageable) 
    {
        Page<ParcelaEmprestimoResponseDTO> parcelas = parcelaEmprestimoService.getParcelasByEmprestimoId(emprestimoId, pageable);
        return ResponseEntity.ok(parcelas);
    }
}
