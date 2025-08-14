package com.sjfjuristas.plataforma.backend.service.Auth;

import com.sjfjuristas.plataforma.backend.dto.Admin.AdminAuthResponseDTO;
import com.sjfjuristas.plataforma.backend.dto.Admin.AdminLoginRequestDTO;

public interface AdminAuthService
{
    AdminAuthResponseDTO login(AdminLoginRequestDTO request);
}