package com.sjfjuristas.plataforma.backend.controller;

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

import com.sjfjuristas.plataforma.backend.domain.Emprestimo;
import com.sjfjuristas.plataforma.backend.domain.Usuario;
import com.sjfjuristas.plataforma.backend.dto.Emprestimos.EmprestimoClienteResponseDTO;
import com.sjfjuristas.plataforma.backend.dto.ParcelaEmprestimo.ParcelaEmprestimoResponseDTO;
import com.sjfjuristas.plataforma.backend.dto.PropostasEmprestimo.PropostaHistoricoResponseDTO;
import com.sjfjuristas.plataforma.backend.repository.EmprestimoRepository;
import com.sjfjuristas.plataforma.backend.service.EmprestimoService;
import com.sjfjuristas.plataforma.backend.service.PropostaHistoricoService;
import com.sjfjuristas.plataforma.backend.service.PropostaService;

@RestController
@RequestMapping("/api/cliente/historico")
public class HistoricoController
{
    @Autowired
    private EmprestimoService emprestimoService;

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    @Autowired
    private PropostaHistoricoService propostaHistoricoService;

    @Autowired
    PropostaService propostaService;

    @GetMapping("/emprestimos")
    public ResponseEntity<Page<EmprestimoClienteResponseDTO>> getHistoricoEmprestimos(@AuthenticationPrincipal Usuario usuarioLogado, @PageableDefault(sort = "dataContratacao", direction = Sort.Direction.DESC) Pageable pageable)
    {
        return ResponseEntity.ok(emprestimoService.getEmprestimosDoCliente(usuarioLogado.getId(), pageable));
    }

    @GetMapping("/emprestimos/{emprestimoId}/parcelas")
    public ResponseEntity<Page<ParcelaEmprestimoResponseDTO>> getHistoricoParcelas( @AuthenticationPrincipal Usuario usuarioLogado, @PathVariable UUID emprestimoId, Pageable pageable)
    {
        return ResponseEntity.ok(emprestimoService.getParcelasDoEmprestimo(emprestimoId, usuarioLogado.getId(), pageable));
    }

    @GetMapping("/emprestimos/propostas")
    public ResponseEntity<Page<PropostaHistoricoResponseDTO>> getHistoricoPropostas( @AuthenticationPrincipal Usuario usuarioLogado, @PathVariable UUID emprestimoId, Pageable pageable)
    {
        Emprestimo emprestimo = emprestimoRepository.findById(emprestimoId).get();
        return ResponseEntity.ok(propostaHistoricoService.findByPropostaId(emprestimo.getPropostaIdPropostasemprestimo().getId(), pageable));
    }
}
