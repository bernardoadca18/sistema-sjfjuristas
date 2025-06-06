package com.sjfjuristas.plataforma.backend.service.Auth;

import com.sjfjuristas.plataforma.backend.dto.Usuario.AuthResponseDTO;
import com.sjfjuristas.plataforma.backend.dto.Usuario.ClienteCreateRequestDTO;
import com.sjfjuristas.plataforma.backend.dto.Usuario.LoginRequestDTO;
// import com.sjfjuristas.plataforma.backend.dto.Usuario.PasswordResetRequestDTO;
// import com.sjfjuristas.plataforma.backend.dto.Usuario.PasswordSetNewWithTokenDTO;

public interface AuthService {
    AuthResponseDTO register(ClienteCreateRequestDTO request);
    AuthResponseDTO login(LoginRequestDTO request);
    // void solicitarRedefinicaoSenha(PasswordResetRequestDTO request);
    // void redefinirSenhaComToken(PasswordSetNewWithTokenDTO request);
}