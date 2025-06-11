package com.sjfjuristas.plataforma.backend.service;

import com.sjfjuristas.plataforma.backend.dto.DocumentoProposta.DocumentoPropostaResponseDTO;
import com.sjfjuristas.plataforma.backend.dto.DocumentoProposta.DocumentoAdminUpdateRequestDTO;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface DocumentoPropostaService {
    DocumentoPropostaResponseDTO salvarDocumento(UUID propostaId, UUID tipoDocumentoId, MultipartFile arquivo, UUID usuarioUploaderId) throws IOException;
    List<DocumentoPropostaResponseDTO> getDocumentosPorProposta(UUID propostaId);
    DocumentoPropostaResponseDTO getDocumentoById(UUID documentoId);
    byte[] downloadDocumento(UUID documentoId) throws IOException; // Ou retornar URL segura
    DocumentoPropostaResponseDTO validarDocumento(UUID documentoId, DocumentoAdminUpdateRequestDTO request, UUID adminId);
    void deleteDocumento(UUID documentoId, UUID usuarioAutenticadoId); // Validar permissão
}