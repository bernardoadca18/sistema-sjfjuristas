package com.sjfjuristas.plataforma.backend.controller;

import com.sjfjuristas.plataforma.backend.dto.Usuario.AuthResponseDTO;
import com.sjfjuristas.plataforma.backend.dto.Usuario.ClienteCreateRequestDTO;
import com.sjfjuristas.plataforma.backend.dto.Usuario.FinalizarCadastroDTO;
import com.sjfjuristas.plataforma.backend.dto.Usuario.LoginRequestDTO;
import com.sjfjuristas.plataforma.backend.dto.Usuario.PreCadastroCheckDTO;
import com.sjfjuristas.plataforma.backend.dto.Usuario.PreCadastroInfoDTO;
import com.sjfjuristas.plataforma.backend.service.Auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController
{
    private final AuthService authService;
    
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody ClienteCreateRequestDTO request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/verificar-pre-cadastro")
    public ResponseEntity<PreCadastroInfoDTO> verificarPreCadastro(@RequestBody @Valid PreCadastroCheckDTO dto)
    {
        PreCadastroInfoDTO response = authService.verificarPreCadastro(dto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/finalizar-cadastro")
    public ResponseEntity<AuthResponseDTO> finalizarCadastro(@RequestBody @Valid FinalizarCadastroDTO data) 
    {
        AuthResponseDTO response = authService.finalizarCadastro(data);
        return ResponseEntity.ok(response);
    }
}