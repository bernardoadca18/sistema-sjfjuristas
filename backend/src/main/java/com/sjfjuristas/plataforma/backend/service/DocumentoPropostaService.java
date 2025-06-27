package com.sjfjuristas.plataforma.backend.service;

import com.sjfjuristas.plataforma.backend.domain.DocumentoProposta;
import com.sjfjuristas.plataforma.backend.domain.PropostaEmprestimo;
import com.sjfjuristas.plataforma.backend.domain.TipoDocumento;
import com.sjfjuristas.plataforma.backend.repository.DocumentoPropostaRepository;
import com.sjfjuristas.plataforma.backend.repository.PropostaEmprestimoRepository;
import com.sjfjuristas.plataforma.backend.repository.TipoDocumentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.time.OffsetDateTime;
import java.util.Map;
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

    @Autowired
    private TipoDocumentoRepository tipoDocumentoRepository;

    @Value("${minio.bucket.documentos}")
    private String docsBucketName;

    @Transactional
    public void salvarDocumentos(UUID propostaId, Map<String, MultipartFile> arquivos) {
        PropostaEmprestimo proposta = propostaRepository.findById(propostaId)
                .orElseThrow(() -> new IllegalArgumentException("Proposta com ID " + propostaId + " não encontrada."));

        arquivos.forEach((nomeCampo, arquivo) -> {
            if (arquivo != null && !arquivo.isEmpty()) {
                TipoDocumento tipoDoc = tipoDocumentoRepository.findByNomeDocumento(mapearNomeParaTipo(nomeCampo))
                        .orElseThrow(() -> new IllegalStateException("Configuração para o tipo de documento '" + nomeCampo + "' não encontrada."));

                salvarArquivoIndividual(proposta, arquivo, tipoDoc);
            }
        });
    }

    private void salvarArquivoIndividual(PropostaEmprestimo proposta, MultipartFile arquivo, TipoDocumento tipoDoc) {
        String subfolder = "proposta-" + proposta.getId().toString();
        String fileUrl = fileStorageService.uploadFile(docsBucketName, arquivo, subfolder);

        DocumentoProposta doc = new DocumentoProposta();
        doc.setPropostaIdPropostasemprestimo(proposta);
        doc.setUsuarioIdUsuarios(proposta.getUsuarioIdUsuarios());
        doc.setTipoDocumentoIdTiposdocumento(tipoDoc);
        doc.setUrlDocumento(fileUrl);
        doc.setNomeArquivoOriginal(arquivo.getOriginalFilename());
        doc.setTipoMime(arquivo.getContentType());
        doc.setTamanhoBytes(arquivo.getSize());
        doc.setDataUpload(OffsetDateTime.now());
        doc.setStatusValidacao("Pendente");

        documentoRepository.save(doc);
    }
    
    private String mapearNomeParaTipo(String nomeFrontend) {
        return switch (nomeFrontend) {
            case "doc_frente" -> "RG/CNH (Frente)";
            case "doc_verso" -> "RG/CNH (Verso)";
            case "comprovante_residencia" -> "Comprovante de Residência";
            case "comprovante_renda" -> "Comprovante de Renda";
            case "selfie" -> "Selfie de Validação";
            default -> throw new IllegalArgumentException("Tipo de arquivo desconhecido: " + nomeFrontend);
        };
    }
    
}
