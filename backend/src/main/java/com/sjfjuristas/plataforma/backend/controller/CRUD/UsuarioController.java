package com.sjfjuristas.plataforma.backend.controller.CRUD;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sjfjuristas.plataforma.backend.dto.CRUD.UsuarioRequestDTO;
import com.sjfjuristas.plataforma.backend.dto.CRUD.UsuarioResponseDTO;
import com.sjfjuristas.plataforma.backend.service.CRUD.UsuarioCRUDService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/admins/usuarios")
public class UsuarioController
{
    @Autowired
    private UsuarioCRUDService usuarioService;

    @GetMapping
    public ResponseEntity<Page<UsuarioResponseDTO>> getAllUsuarios(@PageableDefault(page = 0, size = 10, sort = "nomeCompleto") Pageable pageable)
    {
        return ResponseEntity.ok(usuarioService.getAllUsuarios(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> getUsuarioById(@PathVariable UUID id)
    {
        return ResponseEntity.ok(usuarioService.getUsuarioById(id));
    }

    @GetMapping("/search/{searchTerm}")
    public ResponseEntity<Page<UsuarioResponseDTO>> searchUsuarios(@PathVariable String searchTerm, @PageableDefault(page = 0, size = 10, sort = "nomeCompleto") Pageable pageable)
    {
        return ResponseEntity.ok(usuarioService.searchUsuarios(searchTerm, pageable));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UsuarioResponseDTO> getUsuarioByEmail(@PathVariable String email)
    {
        return ResponseEntity.ok(usuarioService.getUsuarioByEmail(email));
    }
    
    
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> updateUsuario(@PathVariable UUID id, @RequestBody UsuarioRequestDTO dto)
    {
        return ResponseEntity.ok(usuarioService.updateUsuario(id, dto));
    }
    
}   
