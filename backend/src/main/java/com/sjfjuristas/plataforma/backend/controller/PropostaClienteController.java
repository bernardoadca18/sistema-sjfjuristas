package com.sjfjuristas.plataforma.backend.controller;

import com.sjfjuristas.plataforma.backend.domain.PropostaEmprestimo;
import com.sjfjuristas.plataforma.backend.dto.PropostasEmprestimo.PropostaResponseDTO;
import com.sjfjuristas.plataforma.backend.dto.PropostasEmprestimo.RespostaClienteDTO;
import com.sjfjuristas.plataforma.backend.service.PropostaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

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
}
