package com.sjfjuristas.plataforma.backend.controller;

import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.sjfjuristas.plataforma.backend.domain.Usuario;
import com.sjfjuristas.plataforma.backend.dto.Emprestimos.EmprestimoClienteResponseDTO;
import com.sjfjuristas.plataforma.backend.service.EmprestimoService;

@RestController
@RequestMapping("/api/emprestimos")
public class EmprestimoClienteController
{
    @Autowired
    private EmprestimoService emprestimoService;

    @GetMapping
    public ResponseEntity<Page<EmprestimoClienteResponseDTO>> listarMeusEmprestimos(@AuthenticationPrincipal Usuario usuarioLogado, @PageableDefault(sort = "dataContratacao", direction = Sort.Direction.DESC) Pageable pageable)
    {
        UUID clienteId = usuarioLogado.getId();
        
        Page<EmprestimoClienteResponseDTO> pagina = emprestimoService.getEmprestimosDoCliente(clienteId, pageable);
        return ResponseEntity.ok(pagina);
    }
}
