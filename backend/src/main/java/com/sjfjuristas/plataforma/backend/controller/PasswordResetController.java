package com.sjfjuristas.plataforma.backend.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sjfjuristas.plataforma.backend.dto.Usuario.PasswordResetRequestDTO;
import com.sjfjuristas.plataforma.backend.dto.Usuario.PasswordSetNewWithTokenDTO;
import com.sjfjuristas.plataforma.backend.dto.Usuario.PasswordUpdateRequestDTO;
import com.sjfjuristas.plataforma.backend.service.PasswordResetService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/senha")
public class PasswordResetController {

    @Autowired
    private PasswordResetService passwordResetService;

    /**
     * Endpoint para o usuário solicitar a redefinição de senha (fluxo "esqueci a senha").
     */
    @PostMapping("/solicitar-redefinicao")
    public ResponseEntity<Map<String, String>> solicitarRedefinicao(@Valid @RequestBody PasswordResetRequestDTO requestDTO)
    {
        try
        {
            passwordResetService.solicitarRedefinicao(requestDTO);
        } 
        catch (UsernameNotFoundException e) {}

        String message = "Se existir uma conta com o e-mail fornecido, um link para redefinição de senha será enviado.";
        return ResponseEntity.ok(Map.of("message", message));
    }

    /**
     * Endpoint para o usuário definir a nova senha usando o token recebido por e-mail.
     */
    @PostMapping("/redefinir-com-token")
    public ResponseEntity<Map<String, String>> redefinirComToken(@Valid @RequestBody PasswordSetNewWithTokenDTO dto)
    {
        passwordResetService.redefinirComToken(dto);
        return ResponseEntity.ok(Map.of("message", "Senha redefinida com sucesso!"));
    }

    /**
     * Endpoint para o usuário logado atualizar sua própria senha.
     * Este endpoint deve ser protegido pelo Spring Security.
     */
    @PostMapping("/atualizar-logado")
    public ResponseEntity<Map<String, String>> atualizarSenhaLogado(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody PasswordUpdateRequestDTO dto)
    {

        if (userDetails == null)
        {
            return ResponseEntity.status(401).body(Map.of("error", "Usuário não autenticado."));
        }
        
        passwordResetService.atualizarSenhaUsuarioLogado(userDetails.getUsername(), dto);
        return ResponseEntity.ok(Map.of("message", "Senha atualizada com sucesso!"));
    }
}