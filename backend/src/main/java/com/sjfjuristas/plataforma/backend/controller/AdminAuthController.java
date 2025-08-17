package com.sjfjuristas.plataforma.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sjfjuristas.plataforma.backend.dto.Admin.AdminAuthResponseDTO;
import com.sjfjuristas.plataforma.backend.dto.Admin.AdminLoginRequestDTO;
import com.sjfjuristas.plataforma.backend.service.Auth.AdminAuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/auth")
@RequiredArgsConstructor
public class AdminAuthController
{
    private final AdminAuthService adminAuthService;

    @PostMapping("/login")
    public ResponseEntity<AdminAuthResponseDTO> login(@Valid @RequestBody AdminLoginRequestDTO request)
    {
        return ResponseEntity.ok(adminAuthService.login(request));
    }
}