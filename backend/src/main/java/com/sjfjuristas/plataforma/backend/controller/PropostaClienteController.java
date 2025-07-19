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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sjfjuristas.plataforma.backend.domain.PropostaEmprestimo;
import com.sjfjuristas.plataforma.backend.domain.Usuario;
import com.sjfjuristas.plataforma.backend.dto.PropostasEmprestimo.PropostaHistoricoResponseDTO;
import com.sjfjuristas.plataforma.backend.dto.PropostasEmprestimo.PropostaResponseDTO;
import com.sjfjuristas.plataforma.backend.dto.PropostasEmprestimo.RespostaClienteDTO;
import com.sjfjuristas.plataforma.backend.service.PropostaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cliente/propostas")
// TODO: Proteger esta rota para ser acessível apenas por clientes autenticados
public class PropostaClienteController
{
    @Autowired
    private PropostaService propostaService;

    @GetMapping("/{propostaId}")
    public ResponseEntity<PropostaResponseDTO> getPropostaById(@PathVariable UUID propostaId)
    {
        return ResponseEntity.ok(propostaService.findPropostaById(propostaId));
    }

    @GetMapping
    public ResponseEntity<Page<PropostaResponseDTO>> getMyPropostas(@AuthenticationPrincipal Usuario usuarioLogado, @PageableDefault(page = 0, size = 10) Pageable pageable)
    {
        return ResponseEntity.ok(propostaService.getMyPropostas(usuarioLogado.getId(), pageable));
    }

    @GetMapping("/non-paged")
    public ResponseEntity<List<PropostaResponseDTO>> getMyPropostasNonPaged(@AuthenticationPrincipal Usuario usuarioLogado)
    {
        return ResponseEntity.ok(propostaService.getMyPropostasNonPaged(usuarioLogado.getId()));
    }

    @PostMapping("/{propostaId}/responder")
    public ResponseEntity<PropostaResponseDTO> responderContraproposta(@AuthenticationPrincipal Usuario usuarioLogado, @PathVariable UUID propostaId, @Valid @RequestBody RespostaClienteDTO respostaDTO)
    {
        PropostaEmprestimo propostaAtualizada = propostaService.processarRespostaCliente(propostaId, respostaDTO, usuarioLogado.getId());

        return ResponseEntity.ok(new PropostaResponseDTO(propostaAtualizada));
    }

    @GetMapping("/{propostaId}/historico")
    public ResponseEntity<Page<PropostaHistoricoResponseDTO>> getHistoricoPropostaCliente(@PathVariable UUID propostaId, @AuthenticationPrincipal Usuario usuarioLogado, @PageableDefault(page = 0, size = 10, sort = "dataAlteracao", direction = Sort.Direction.DESC) Pageable pageable)
    {
        UUID clienteId = usuarioLogado.getId();

        Page<PropostaHistoricoResponseDTO> historico = propostaService.getHistoricoPropostaCliente(propostaId, clienteId, pageable);
        return ResponseEntity.ok(historico);
    }
}
