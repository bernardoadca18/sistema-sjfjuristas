package com.sjfjuristas.plataforma.backend.service;

import com.sjfjuristas.plataforma.backend.domain.DocumentoProposta;
import com.sjfjuristas.plataforma.backend.domain.PropostaEmprestimo;
import com.sjfjuristas.plataforma.backend.dto.DocumentoProposta.DocumentoPropostaResponseDTO;
import com.sjfjuristas.plataforma.backend.repository.DocumentoPropostaRepository;
import com.sjfjuristas.plataforma.backend.repository.PropostaEmprestimoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DocumentoPropostaService 
{
    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private DocumentoPropostaRepository documentoRepository;

    @Autowired
    private PropostaEmprestimoRepository propostaRepository;


    @Transactional
    public List<DocumentoPropostaResponseDTO> salvarDocumentos(UUID propostaId, MultipartFile[] files)
    {
        PropostaEmprestimo proposta = propostaRepository.findById(propostaId)
                .orElseThrow(() -> new IllegalArgumentException("Proposta com ID " + propostaId + " não encontrada."));

        List<DocumentoPropostaResponseDTO> responses = new ArrayList<>();

        for (MultipartFile file : files) {
            String subfolder = "proposta-" + propostaId.toString();
            String fileUrl = fileStorageService.uploadFile(file, subfolder);

            DocumentoProposta doc = new DocumentoProposta();
            doc.setPropostaIdPropostasemprestimo(proposta);
            doc.setUsuarioIdUsuarios(proposta.getUsuarioIdUsuarios());
            doc.setUrlDocumento(fileUrl);
            doc.setNomeArquivoOriginal(file.getOriginalFilename());
            doc.setTipoMime(file.getContentType());
            doc.setTamanhoBytes(file.getSize());
            doc.setDataUpload(OffsetDateTime.now());
            doc.setStatusValidacao("Pendente");
            
            // O tipo de documento será definido posteriormente pela equipe de análise
            doc.setTipoDocumentoIdTiposdocumento(null);

            DocumentoProposta savedDoc = documentoRepository.save(doc);
            
            responses.add(new DocumentoPropostaResponseDTO(
                savedDoc.getId(),
                "Não categorizado", // Tipo será definido na análise
                savedDoc.getNomeArquivoOriginal(),
                savedDoc.getUrlDocumento(),
                savedDoc.getDataUpload(),
                savedDoc.getStatusValidacao(),
                null
            ));
        }

        return responses;
    }
}
