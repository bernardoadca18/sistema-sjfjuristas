package com.sjfjuristas.plataforma.backend.service;

import com.sjfjuristas.plataforma.backend.dto.Usuario.ClienteResponseDTO;
import com.sjfjuristas.plataforma.backend.dto.Usuario.ClienteUpdateRequestDTO;
import com.sjfjuristas.plataforma.backend.dto.Usuario.PasswordUpdateRequestDTO;
import java.util.UUID;

public interface ClienteService {
    ClienteResponseDTO getClienteInfo(UUID usuarioId);
    ClienteResponseDTO updateClienteInfo(UUID usuarioId, ClienteUpdateRequestDTO request);
    void updatePassword(UUID usuarioId, PasswordUpdateRequestDTO request);
    // Outros métodos como desativar conta, etc.
}