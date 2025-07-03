package com.sjfjuristas.plataforma.backend.controller;

import com.sjfjuristas.plataforma.backend.domain.Usuario;
import com.sjfjuristas.plataforma.backend.dto.ChavesPixUsuario.ChavePixCreateRequestDTO;
import com.sjfjuristas.plataforma.backend.dto.ChavesPixUsuario.ChavePixResponseDTO;
import com.sjfjuristas.plataforma.backend.service.ChavePixUsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cliente/chaves-pix")
public class ChavePixUsuarioController {

    @Autowired
    private ChavePixUsuarioService chavePixService;

    @GetMapping
    public ResponseEntity<List<ChavePixResponseDTO>> getMinhasChavesPix(@AuthenticationPrincipal Usuario usuarioLogado) {
        return ResponseEntity.ok(chavePixService.getChavesPixDoUsuario(usuarioLogado));
    }

    @PostMapping
    public ResponseEntity<ChavePixResponseDTO> addMinhaChavePix(@AuthenticationPrincipal Usuario usuarioLogado, @Valid @RequestBody ChavePixCreateRequestDTO dto) {
        ChavePixResponseDTO response = chavePixService.addChavePix(usuarioLogado, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PutMapping("/{chaveId}/ativar")
    public ResponseEntity<Void> setMinhaChaveAtiva(@AuthenticationPrincipal Usuario usuarioLogado, @PathVariable UUID chaveId) {
        chavePixService.setChaveAtiva(usuarioLogado, chaveId);
        return ResponseEntity.noContent().build();
    }
}

