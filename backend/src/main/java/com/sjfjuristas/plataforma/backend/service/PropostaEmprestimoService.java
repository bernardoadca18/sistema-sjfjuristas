package com.sjfjuristas.plataforma.backend.service;

import com.sjfjuristas.plataforma.backend.dto.PropostasEmprestimo.PropostaEmprestimoCreateLPRequestDTO;
import com.sjfjuristas.plataforma.backend.dto.PropostasEmprestimo.PropostaEmprestimoResponseDTO;
import com.sjfjuristas.plataforma.backend.dto.PropostasEmprestimo.PropostaEmprestimoAdminActionRequestDTO;
import java.util.List;
import java.util.UUID;
// import com.sjfjuristas.plataforma.backend.dto.Documento.DocumentoUploadRequestDTO;
// import com.sjfjuristas.plataforma.backend.dto.Documento.DocumentoPropostaResponseDTO;
// import org.springframework.web.multipart.MultipartFile;

public interface PropostaEmprestimoService {
    PropostaEmprestimoResponseDTO criarPropostaLandingPage(PropostaEmprestimoCreateLPRequestDTO request);
    PropostaEmprestimoResponseDTO getPropostaById(UUID propostaId);
    List<PropostaEmprestimoResponseDTO> getAllPropostas(); // Pode precisar de paginação
    List<PropostaEmprestimoResponseDTO> getPropostasPorCliente(UUID clienteId);
    PropostaEmprestimoResponseDTO processarAcaoAdmin(UUID propostaId, PropostaEmprestimoAdminActionRequestDTO request, UUID adminId);
    
    // DocumentoPropostaResponseDTO adicionarDocumentoProposta(UUID propostaId, DocumentoUploadRequestDTO docRequest, MultipartFile arquivo);
}
