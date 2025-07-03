package com.sjfjuristas.plataforma.backend.controller;

import com.sjfjuristas.plataforma.backend.domain.Usuario;
import com.sjfjuristas.plataforma.backend.dto.Usuario.ClienteResponseDTO;
import com.sjfjuristas.plataforma.backend.dto.Usuario.ClienteUpdateRequestDTO;
import com.sjfjuristas.plataforma.backend.dto.Usuario.PasswordUpdateRequestDTO;
import com.sjfjuristas.plataforma.backend.service.UsuarioClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cliente/perfil")
public class UsuarioClienteController
{
    @Autowired
    private UsuarioClienteService usuarioClienteService;

    @GetMapping
    public ResponseEntity<ClienteResponseDTO> getMeuPerfil(@AuthenticationPrincipal Usuario usuarioLogado) {
        ClienteResponseDTO dto = usuarioClienteService.getDadosCliente(usuarioLogado.getId());
        return ResponseEntity.ok(dto);
    }

    @PutMapping
    public ResponseEntity<ClienteResponseDTO> updateMeuPerfil(@AuthenticationPrincipal Usuario usuarioLogado, @Valid @RequestBody ClienteUpdateRequestDTO requestDTO) {
        ClienteResponseDTO dto = usuarioClienteService.updateDadosCliente(usuarioLogado.getId(), requestDTO);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/alterar-senha")
    public ResponseEntity<Void> updateMinhaSenha(@AuthenticationPrincipal Usuario usuarioLogado, @Valid @RequestBody PasswordUpdateRequestDTO requestDTO) {
        usuarioClienteService.updateSenha(usuarioLogado.getId(), requestDTO);
        return ResponseEntity.noContent().build();
    }
}