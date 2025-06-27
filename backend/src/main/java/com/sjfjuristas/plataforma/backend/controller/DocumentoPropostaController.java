package com.sjfjuristas.plataforma.backend.controller;

import com.sjfjuristas.plataforma.backend.service.DocumentoPropostaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/propostas")
@CrossOrigin(origins = "*") // Restringir em prod
public class DocumentoPropostaController 
{
    @Autowired
    private DocumentoPropostaService documentoPropostaService;

    @PostMapping(value = "/{propostaId}/documentos", consumes = "multipart/form-data")
    public ResponseEntity<Void> uploadDocumentos(
            @PathVariable UUID propostaId,
            @RequestParam(value = "doc_frente", required = false) MultipartFile docFrente,
            @RequestParam(value = "doc_verso", required = false) MultipartFile docVerso,
            @RequestParam(value = "comprovante_residencia", required = false) MultipartFile compResidencia,
            @RequestParam(value = "comprovante_renda", required = false) MultipartFile compRenda,
            @RequestParam(value = "selfie", required = false) MultipartFile selfie) {

        Map<String, MultipartFile> arquivosRecebidos = new HashMap<>();
        if (docFrente != null) arquivosRecebidos.put("doc_frente", docFrente);
        if (docVerso != null) arquivosRecebidos.put("doc_verso", docVerso);
        if (compResidencia != null) arquivosRecebidos.put("comprovante_residencia", compResidencia);
        if (compRenda != null) arquivosRecebidos.put("comprovante_renda", compRenda);
        if (selfie != null) arquivosRecebidos.put("selfie", selfie);

        if (arquivosRecebidos.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        documentoPropostaService.salvarDocumentos(propostaId, arquivosRecebidos);
        return ResponseEntity.ok().build();
    }
}
