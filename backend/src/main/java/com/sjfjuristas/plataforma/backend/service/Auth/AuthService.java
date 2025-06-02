package com.sjfjuristas.plataforma.backend.service.Auth;

import com.sjfjuristas.plataforma.backend.dto.Usuario.AuthResponseDTO;
import com.sjfjuristas.plataforma.backend.dto.Usuario.ClienteCreateRequestDTO;
import com.sjfjuristas.plataforma.backend.dto.Usuario.LoginRequestDTO;

public interface AuthService {
    AuthResponseDTO register(ClienteCreateRequestDTO request);
    AuthResponseDTO login(LoginRequestDTO request);
}