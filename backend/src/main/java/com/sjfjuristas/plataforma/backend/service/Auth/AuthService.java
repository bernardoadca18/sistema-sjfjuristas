package com.sjfjuristas.plataforma.backend.service.Auth;

import com.sjfjuristas.plataforma.backend.dto.Usuario.AuthResponseDTO;
import com.sjfjuristas.plataforma.backend.dto.Usuario.ClienteCreateRequestDTO;
import com.sjfjuristas.plataforma.backend.dto.Usuario.FinalizarCadastroDTO;
import com.sjfjuristas.plataforma.backend.dto.Usuario.LoginRequestDTO;
// import com.sjfjuristas.plataforma.backend.dto.Usuario.PasswordResetRequestDTO;
// import com.sjfjuristas.plataforma.backend.dto.Usuario.PasswordSetNewWithTokenDTO;
import com.sjfjuristas.plataforma.backend.dto.Usuario.PreCadastroCheckDTO;
import com.sjfjuristas.plataforma.backend.dto.Usuario.PreCadastroInfoDTO;

public interface AuthService {
    AuthResponseDTO register(ClienteCreateRequestDTO request);
    AuthResponseDTO login(LoginRequestDTO request);
    // void solicitarRedefinicaoSenha(PasswordResetRequestDTO request);
    // void redefinirSenhaComToken(PasswordSetNewWithTokenDTO request);

    PreCadastroInfoDTO verificarPreCadastro(PreCadastroCheckDTO request);
    AuthResponseDTO finalizarCadastro(FinalizarCadastroDTO request);
}