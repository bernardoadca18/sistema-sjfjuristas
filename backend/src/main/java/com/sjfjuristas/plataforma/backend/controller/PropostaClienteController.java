package com.sjfjuristas.plataforma.backend.controller;

import com.sjfjuristas.plataforma.backend.domain.PropostaEmprestimo;
import com.sjfjuristas.plataforma.backend.dto.PropostasEmprestimo.PropostaHistoricoResponseDTO;
import com.sjfjuristas.plataforma.backend.dto.PropostasEmprestimo.PropostaResponseDTO;
import com.sjfjuristas.plataforma.backend.dto.PropostasEmprestimo.RespostaClienteDTO;
import com.sjfjuristas.plataforma.backend.service.PropostaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;
import java.util.UUID;
import com.sjfjuristas.plataforma.backend.domain.Usuario;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/api/cliente/propostas")
// TODO: Proteger esta rota para ser acessível apenas por clientes autenticados
public class PropostaClienteController
{
    @Autowired
    private PropostaService propostaService;

    @PostMapping("/{propostaId}/responder")
    public ResponseEntity<PropostaResponseDTO> responderContraproposta(@PathVariable UUID propostaId, @Valid @RequestBody RespostaClienteDTO respostaDTO)
    {
        PropostaEmprestimo propostaAtualizada = propostaService.processarRespostaCliente(propostaId, respostaDTO);

        PropostaResponseDTO response = new PropostaResponseDTO(
            propostaAtualizada.getId(),
            propostaAtualizada.getValorOfertado() != null ? propostaAtualizada.getValorOfertado() : propostaAtualizada.getValorSolicitado(),
            propostaAtualizada.getNomeCompletoSolicitante(),
            propostaAtualizada.getEmailSolicitante(),
            propostaAtualizada.getDataSolicitacao(),
            propostaAtualizada.getStatusPropostaIdStatusproposta().getNomeStatus()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{propostaId}/historico")
    public ResponseEntity<Page<PropostaHistoricoResponseDTO>> getHistoricoPropostaCliente(@PathVariable UUID propostaId, @AuthenticationPrincipal Usuario usuarioLogado, @PageableDefault(page = 0, size = 10, sort = "dataAlteracao", direction = Sort.Direction.DESC) Pageable pageable)
    {
        UUID clienteId = usuarioLogado.getId();

        Page<PropostaHistoricoResponseDTO> historico = propostaService.getHistoricoPropostaCliente(propostaId, clienteId, pageable);
        return ResponseEntity.ok(historico);
    }
}
