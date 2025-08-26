package com.sjfjuristas.plataforma.backend.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sjfjuristas.plataforma.backend.domain.Administrador;
import com.sjfjuristas.plataforma.backend.dto.Admin.AdminResponseDTO;

@RestController
@RequestMapping("/api/admin/profile")
public class AdminProfileController
{
    @GetMapping("/me")
    public ResponseEntity<AdminResponseDTO> getMyProfile(@AuthenticationPrincipal Administrador adminLogado)
    {
        if (adminLogado == null)
        {
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(new AdminResponseDTO(adminLogado));
    }
}
