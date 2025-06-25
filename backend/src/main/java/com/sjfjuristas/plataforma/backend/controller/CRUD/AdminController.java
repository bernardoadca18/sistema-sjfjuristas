package com.sjfjuristas.plataforma.backend.controller.CRUD;

import com.sjfjuristas.plataforma.backend.dto.Admin.AdminCreateRequestDTO;
import com.sjfjuristas.plataforma.backend.dto.Admin.AdminResponseDTO;
import com.sjfjuristas.plataforma.backend.dto.Admin.AdminUpdateRequestDTO;
import com.sjfjuristas.plataforma.backend.service.CRUD.AdminCRUDService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties.Http;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admins")
// TODO: Adicionar segurança para permitir acesso apenas a Super Administradores
public class AdminController
{
    @Autowired
    private AdminCRUDService adminCRUDService;

    @PostMapping
    public ResponseEntity<AdminResponseDTO> createAdmin(@Valid @RequestBody AdminCreateRequestDTO request)
    {
        AdminResponseDTO response = adminCRUDService.createAdmin(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminResponseDTO> getAdminById(@PathVariable UUID id)
    {
        AdminResponseDTO response = adminCRUDService.getAdminById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<AdminResponseDTO>> getAllAdmins(@PageableDefault(page = 0, size = 10, sort = "nomeCompleto") Pageable pageable)
    {
        Page<AdminResponseDTO> response = adminCRUDService.getAllAdmins(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping(params = "ativo")
    public ResponseEntity<List<AdminResponseDTO>> getAdminsByStatus(@RequestParam boolean ativo)
    {
        List<AdminResponseDTO> response = adminCRUDService.getAdminsByStatus(ativo);
        return ResponseEntity.ok(response);
    }

    @GetMapping(params = "email")
    public ResponseEntity<AdminResponseDTO> getAdminByEmail(@RequestParam String email)
    {
        AdminResponseDTO response = adminCRUDService.getAdminByEmail(email);
        return ResponseEntity.ok(response);
    }

    @GetMapping(params = "matricula")
    public ResponseEntity<AdminResponseDTO> getAdminByMatricula(@RequestParam String matricula)
    {
        AdminResponseDTO response = adminCRUDService.getAdminByMatriculaFuncionario(matricula);
        return ResponseEntity.ok(response);
    }

    @GetMapping(params = "cargo")
    public ResponseEntity<List<AdminResponseDTO>> getAdminsByCargoInterno(@RequestParam String cargoInterno)
    {
        List<AdminResponseDTO> response = adminCRUDService.getAdminsByCargoInterno(cargoInterno);
        return ResponseEntity.ok(response);
    }

    @GetMapping(params = "departamento")
    public ResponseEntity<List<AdminResponseDTO>> getAdminsByDepartamento(@RequestParam String departamento)
    {
        List<AdminResponseDTO> response = adminCRUDService.getAdminsByDepartamento(departamento);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminResponseDTO> updateAdmin(@PathVariable UUID id, @Valid @RequestBody AdminUpdateRequestDTO request)
    {
        AdminResponseDTO response = adminCRUDService.updateAdmin(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable UUID id)
    {
        adminCRUDService.deleteAdmin(id);
        return ResponseEntity.noContent().build();
    }
}
