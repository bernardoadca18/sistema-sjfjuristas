package com.sjfjuristas.plataforma.backend.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sjfjuristas.plataforma.backend.domain.Usuario;
import com.sjfjuristas.plataforma.backend.dto.ChavesPixUsuario.ChavePixCreateRequestDTO;
import com.sjfjuristas.plataforma.backend.dto.ChavesPixUsuario.ChavePixResponseDTO;
import com.sjfjuristas.plataforma.backend.repository.UsuarioRepository;
import com.sjfjuristas.plataforma.backend.service.ChavePixUsuarioService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cliente/chaves-pix")
public class ChavePixUsuarioController
{
    @Autowired
    private ChavePixUsuarioService chavePixService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public ResponseEntity<List<ChavePixResponseDTO>> getMinhasChavesPix(@AuthenticationPrincipal Usuario usuarioLogado)
    {
        return ResponseEntity.ok(chavePixService.getChavesPixDoUsuario(usuarioLogado));
    }

    @PostMapping
    public ResponseEntity<ChavePixResponseDTO> addMinhaChavePix(@AuthenticationPrincipal Usuario usuarioLogado, @Valid @RequestBody ChavePixCreateRequestDTO dto)
    {
        ChavePixResponseDTO response = chavePixService.addChavePix(usuarioLogado, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/add-to/{userId}")
    public ResponseEntity<ChavePixResponseDTO> addMinhaChavePixSignUp(@PathVariable UUID userId, @Valid @RequestBody ChavePixCreateRequestDTO dto)
    {
        Usuario usuario;
        usuario = usuarioRepository.findById(userId).get();

        ChavePixResponseDTO response = chavePixService.addChavePix(usuario, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PutMapping("/{chaveId}/ativar")
    public ResponseEntity<Void> setMinhaChaveAtiva(@AuthenticationPrincipal Usuario usuarioLogado, @PathVariable UUID chaveId)
    {
        chavePixService.setChaveAtiva(usuarioLogado, chaveId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{chaveId}")
    public ResponseEntity<Void> deleteMinhaChavePix(@AuthenticationPrincipal Usuario usuarioLogado, @PathVariable UUID chaveId)
    {
        chavePixService.deleteChavePix(usuarioLogado, chaveId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/ativa")
    public ResponseEntity<ChavePixResponseDTO> getMinhaChaveAtiva(@AuthenticationPrincipal Usuario usuarioLogado)
    {
        try 
        {
            ChavePixResponseDTO chaveAtiva = chavePixService.getChaveAtiva(usuarioLogado);
            return ResponseEntity.ok(chaveAtiva);
        } 
        catch (EntityNotFoundException e) 
        {
            return ResponseEntity.notFound().build();
        }
    }
}

