package com.sjfjuristas.plataforma.backend.controller.CRUD;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sjfjuristas.plataforma.backend.dto.CRUD.PerfilUsuarioRequestDTO;
import com.sjfjuristas.plataforma.backend.dto.CRUD.PerfilUsuarioResponseDTO;
import com.sjfjuristas.plataforma.backend.service.CRUD.PerfilUsuarioCRUDService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/perfis-usuario")
public class PerfilUsuarioCRUDController
{
    @Autowired
    private PerfilUsuarioCRUDService perfilUsuarioCRUDService;

    @GetMapping
    public ResponseEntity<Page<PerfilUsuarioResponseDTO>> getAllPerfisUsuario(@PageableDefault(page = 0, size = 10, sort = "nomePerfil") Pageable pageable)
    {
        Page<PerfilUsuarioResponseDTO> response = perfilUsuarioCRUDService.getAllPerfisUsuarios(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/non-paged")
    public ResponseEntity<List<PerfilUsuarioResponseDTO>> getAllPerfisUsuarioNonPaged()
    {
        List<PerfilUsuarioResponseDTO> response = perfilUsuarioCRUDService.getAllPerfisUsuarios();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PerfilUsuarioResponseDTO> getPerfilUsuarioById(@PathVariable UUID id)
    {
        PerfilUsuarioResponseDTO response = perfilUsuarioCRUDService.getPerfilUsuarioById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/nome-perfil/{nomePerfil}")
    public ResponseEntity<PerfilUsuarioResponseDTO> getPerfilUsuarioByNomePerfil(@PathVariable String nomePerfil)
    {
        PerfilUsuarioResponseDTO response = perfilUsuarioCRUDService.getPerfilUsuarioByNomePerfil(nomePerfil);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<PerfilUsuarioResponseDTO> createPerfilUsuario(@Valid @RequestBody PerfilUsuarioRequestDTO dto)
    {
        PerfilUsuarioResponseDTO response = perfilUsuarioCRUDService.createPerfilUsuario(dto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PerfilUsuarioResponseDTO> updatePerfilUsuario(@PathVariable UUID id, @Valid @RequestBody PerfilUsuarioRequestDTO dto)
    {
        PerfilUsuarioResponseDTO response = perfilUsuarioCRUDService.updatePerfilUsuario(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PerfilUsuarioResponseDTO> deletePerfilUsuario(@PathVariable UUID id)
    {
        perfilUsuarioCRUDService.deletePerfilUsuario(id);
        return ResponseEntity.noContent().build();
    }
    
}