package com.sjfjuristas.plataforma.backend.controller.CRUD;

import com.sjfjuristas.plataforma.backend.dto.Admin.AdminUpdatePasswordRequestDTO;
import com.sjfjuristas.plataforma.backend.dto.Admin.AdminUpdatePasswordWithOldPasswordDTO;
import com.sjfjuristas.plataforma.backend.dto.Admin.AdminUpdatePasswordWithTokenDTO;
import com.sjfjuristas.plataforma.backend.dto.Usuario.PasswordResetRequestDTO;
import com.sjfjuristas.plataforma.backend.dto.Usuario.PasswordSetNewWithTokenDTO;
import com.sjfjuristas.plataforma.backend.dto.Usuario.PasswordUpdateRequestDTO;
import com.sjfjuristas.plataforma.backend.service.CRUD.AdminPasswordService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/password")
public class AdminPasswordController
{
    @Autowired
    private AdminPasswordService adminPasswordService;

    @PostMapping("/request-reset")
    public ResponseEntity<String> requestPasswordReset(@Valid @RequestBody AdminUpdatePasswordRequestDTO request)
    {
        adminPasswordService.requestPasswordReset(request);
        return ResponseEntity.ok("Solicitação de redefinição de senha enviada com sucesso. Se um usuário com este e-mail existir, um link para redefinição de senha foi enviado.");
    }

    @PostMapping("/reset-with-token")
    public ResponseEntity<String> resetPasswordWithToken(@Valid @RequestBody AdminUpdatePasswordWithTokenDTO request)
    {
        adminPasswordService.resetPasswordWithToken(request);
        return ResponseEntity.ok("Senha redefinida com sucesso.");
    }

    @PostMapping("/update")
    public ResponseEntity<String> updatePassword(@Valid @RequestBody AdminUpdatePasswordWithOldPasswordDTO request)
    {
        adminPasswordService.updatePassword(request);
        return ResponseEntity.ok("Senha atualizada com sucesso.");
    }
}
