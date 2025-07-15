package com.sjfjuristas.plataforma.backend.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sjfjuristas.plataforma.backend.domain.ParcelaEmprestimo;
import com.sjfjuristas.plataforma.backend.domain.Usuario;
import com.sjfjuristas.plataforma.backend.dto.Emprestimos.EmprestimoClienteResponseDTO;
import com.sjfjuristas.plataforma.backend.dto.ParcelaEmprestimo.ParcelaEmprestimoResponseDTO;
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

    

    @GetMapping("/{emprestimoId}")
    public ResponseEntity<EmprestimoClienteResponseDTO> getMeuEmprestimoPorId(@AuthenticationPrincipal Usuario usuarioLogado, @PathVariable UUID emprestimoId)
    {
        EmprestimoClienteResponseDTO dto = emprestimoService.getEmprestimoDoCliente(emprestimoId, usuarioLogado.getId());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{emprestimoId}/parcelas")
    public ResponseEntity<Page<ParcelaEmprestimoResponseDTO>> getParcelasDoMeuEmprestimo( @AuthenticationPrincipal Usuario usuarioLogado, @PathVariable UUID emprestimoId, Pageable pageable) 
    {
        Page<ParcelaEmprestimoResponseDTO> pagina = emprestimoService.getParcelasDoEmprestimo(emprestimoId, usuarioLogado.getId(), pageable);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping("/{emprestimoId}/widget-parcelas")
    public ResponseEntity<List<ParcelaEmprestimoResponseDTO>> getParcelasForWidget(@PathVariable UUID emprestimoId)
    {
        List<ParcelaEmprestimo> parcelas = emprestimoService.getParcelasParaWidget(emprestimoId);
        List<ParcelaEmprestimoResponseDTO> dtos = parcelas.stream().map(ParcelaEmprestimoResponseDTO::new).toList();

        return ResponseEntity.ok(dtos);
    }
}
