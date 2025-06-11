package com.sjfjuristas.plataforma.backend.service.impl;

import com.sjfjuristas.plataforma.backend.domain.DocumentoProposta;
import com.sjfjuristas.plataforma.backend.domain.PropostaEmprestimo;
import com.sjfjuristas.plataforma.backend.domain.TipoDocumento;
import com.sjfjuristas.plataforma.backend.domain.Usuario;
import com.sjfjuristas.plataforma.backend.dto.DocumentoProposta.DocumentoPropostaResponseDTO;
import com.sjfjuristas.plataforma.backend.dto.DocumentoProposta.DocumentoAdminUpdateRequestDTO;
import com.sjfjuristas.plataforma.backend.repository.DocumentoPropostaRepository;
import com.sjfjuristas.plataforma.backend.repository.PropostaEmprestimoRepository;
import com.sjfjuristas.plataforma.backend.repository.TipoDocumentoRepository;
import com.sjfjuristas.plataforma.backend.repository.UsuarioRepository;
import com.sjfjuristas.plataforma.backend.service.DocumentoPropostaService;
// import com.sjfjuristas.plataforma.backend.service.FileStorageService; 
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentoPropostaServiceImpl implements DocumentoPropostaService {

    private static final Logger logger = LoggerFactory.getLogger(DocumentoPropostaServiceImpl.class);

    private final DocumentoPropostaRepository documentoRepository;
    private final PropostaEmprestimoRepository propostaRepository;
    private final TipoDocumentoRepository tipoDocumentoRepository;
    private final UsuarioRepository usuarioRepository; 
    // private final FileStorageService fileStorageService; // Seu serviço para interagir com S3, etc.

    @Override
    @Transactional
    public DocumentoPropostaResponseDTO salvarDocumento(UUID propostaId, UUID tipoDocumentoId, MultipartFile arquivo, UUID usuarioUploaderId) throws IOException {
        PropostaEmprestimo proposta = propostaRepository.findById(propostaId)
            .orElseThrow(() -> new EntityNotFoundException("Proposta não encontrada: " + propostaId));
        TipoDocumento tipoDocumento = tipoDocumentoRepository.findById(tipoDocumentoId) 
            .orElseThrow(() -> new EntityNotFoundException("Tipo de Documento não encontrado: " + tipoDocumentoId));
        Usuario uploader = usuarioRepository.findById(usuarioUploaderId)
            .orElseThrow(() -> new EntityNotFoundException("Usuário uploader não encontrado: " + usuarioUploaderId));

        // Lógica para salvar o arquivo no storage (ex: S3) e obter a URL
        // String urlDoArquivo = fileStorageService.uploadFile(arquivo, "documentos-propostas/" + propostaId + "/");
        String urlDoArquivo = ""; // Placeholder
        logger.info("Arquivo {} salvo no storage. URL: {}", arquivo.getOriginalFilename(), urlDoArquivo);


        DocumentoProposta doc = new DocumentoProposta();
        doc.setPropostaIdPropostasemprestimo(proposta); // Ajuste nome
        doc.setTipoDocumentoIdTiposdocumento(tipoDocumento); // Ajuste nome
        doc.setUrlDocumento(urlDoArquivo);
        doc.setNomeArquivoOriginal(arquivo.getOriginalFilename());
        doc.setTipoMime(arquivo.getContentType());
        doc.setTamanhoBytes(arquivo.getSize());
        doc.setUsuarioIdUsuarios(uploader); // Quem fez o upload
        doc.setStatusValidacao("Pendente");

        DocumentoProposta savedDoc = documentoRepository.save(doc);
        return mapToResponseDTO(savedDoc);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DocumentoPropostaResponseDTO> getDocumentosPorProposta(UUID propostaId) {
        PropostaEmprestimo proposta = propostaRepository.findById(propostaId)
            .orElseThrow(() -> new EntityNotFoundException("Proposta não encontrada: " + propostaId));
        return documentoRepository.findByPropostaIdPropostasEmprestimo(proposta) // Ajuste nome
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public DocumentoPropostaResponseDTO getDocumentoById(UUID documentoId){
        DocumentoProposta doc = documentoRepository.findById(documentoId)
            .orElseThrow(() -> new EntityNotFoundException("Documento não encontrado: " + documentoId));
        return mapToResponseDTO(doc);
    }

    @Override
    public byte[] downloadDocumento(UUID documentoId) throws IOException {
        DocumentoProposta doc = documentoRepository.findById(documentoId)
            .orElseThrow(() -> new EntityNotFoundException("Documento não encontrado: " + documentoId));
        // return fileStorageService.downloadFile(doc.getUrlDocumento()); // Implementar
        logger.warn("Download de arquivo não implementado. URL do doc: {}", doc.getUrlDocumento());
        throw new UnsupportedOperationException("Download de arquivo não implementado.");
    }

    @Override
    @Transactional
    public DocumentoPropostaResponseDTO validarDocumento(UUID documentoId, DocumentoAdminUpdateRequestDTO request, UUID adminId) {
        DocumentoProposta doc = documentoRepository.findById(documentoId)
            .orElseThrow(() -> new EntityNotFoundException("Documento não encontrado: " + documentoId));
        /*Usuario admin = usuarioRepository.findById(adminId)
            .orElseThrow(() -> new EntityNotFoundException("Administrador não encontrado: " + adminId));*/
            
        // Validar se admin pode validar, etc.
        
        doc.setStatusValidacao(request.getStatusValidacao());
        doc.setObservacoesValidacao(request.getObservacoesValidacao());
        // doc.setValidadoPorAdminId(admin); // Se você tem um campo para isso na entidade DocumentoProposta
        
        DocumentoProposta updatedDoc = documentoRepository.save(doc);
        return mapToResponseDTO(updatedDoc);
    }

    @Override
    @Transactional
    public void deleteDocumento(UUID documentoId, UUID usuarioAutenticadoId) {
        // Adicionar lógica de permissão e deleção do arquivo no storage
        logger.warn("Deleção de documento e arquivo no storage não implementada.");
        documentoRepository.deleteById(documentoId);
    }

    private DocumentoPropostaResponseDTO mapToResponseDTO(DocumentoProposta doc) {
        // Implementar mapeamento
        return DocumentoPropostaResponseDTO.builder()
                .id(doc.getId())
                .tipoDocumentoNome(doc.getTipoDocumentoIdTiposdocumento() != null ? doc.getTipoDocumentoIdTiposdocumento().getNomeDocumento() : null)
                .nomeArquivoOriginal(doc.getNomeArquivoOriginal())
                .dataUpload(doc.getDataUpload())
                .statusValidacao(doc.getStatusValidacao())
                //.urlVisualizacao(gerarUrlSegura(doc.getUrlDocumento())) // Gerar URL temporária se necessário
                .build();
    }
}
