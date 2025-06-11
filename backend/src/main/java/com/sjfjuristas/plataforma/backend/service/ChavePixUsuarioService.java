package com.sjfjuristas.plataforma.backend.service;

import com.sjfjuristas.plataforma.backend.dto.ChavesPixUsuario.ChavePixCreateRequestDTO;
import com.sjfjuristas.plataforma.backend.dto.ChavesPixUsuario.ChavePixResponseDTO;
import com.sjfjuristas.plataforma.backend.dto.ChavesPixUsuario.ChavePixUpdateRequestDTO;
import java.util.List;
import java.util.UUID;

public interface ChavePixUsuarioService {
    ChavePixResponseDTO adicionarChavePix(UUID usuarioId, ChavePixCreateRequestDTO request);
    List<ChavePixResponseDTO> getChavesPixPorUsuario(UUID usuarioId);
    ChavePixResponseDTO getChavePixById(UUID chavePixId);
    ChavePixResponseDTO updateChavePix(UUID chavePixId, ChavePixUpdateRequestDTO request); // Ex: para tornar ativa
    void deleteChavePix(UUID chavePixId, UUID usuarioIdAutenticado); // Validar permissão
    // void verificarChavePix(UUID chavePixId); // Processo de verificação
}