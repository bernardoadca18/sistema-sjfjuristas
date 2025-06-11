package com.sjfjuristas.plataforma.backend.service;

import com.sjfjuristas.plataforma.backend.dto.Admin.AdminCreateRequestDTO;
import com.sjfjuristas.plataforma.backend.dto.Admin.AdminUpdateRequestDTO;
import com.sjfjuristas.plataforma.backend.dto.Admin.AdminResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface AdministradorService {
    AdminResponseDTO criarAdministrador(AdminCreateRequestDTO request);
    AdminResponseDTO getAdministradorById(UUID adminId);
    Page<AdminResponseDTO> getAllAdministradores(Pageable pageable);
    AdminResponseDTO updateAdministrador(UUID adminId, AdminUpdateRequestDTO request);
    void deleteAdministrador(UUID adminId);
    
}