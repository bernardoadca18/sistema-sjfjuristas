package com.sjfjuristas.plataforma.backend.controller;

import com.sjfjuristas.plataforma.backend.dto.DocumentoProposta.DocumentoPropostaResponseDTO;
import com.sjfjuristas.plataforma.backend.service.DocumentoPropostaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/propostas")
@CrossOrigin(origins = "*") // Restringir em prod
public class DocumentoPropostaController 
{
    @Autowired
    private DocumentoPropostaService documentoPropostaService;

    @PostMapping("/{propostaId}/documentos")
    public ResponseEntity<List<DocumentoPropostaResponseDTO>> uploadDocumentos(@PathVariable UUID propostaId, @RequestParam("files") MultipartFile[] files) {
        
        if (files == null || files.length == 0) {
            return ResponseEntity.badRequest().build();
        }
        
        List<DocumentoPropostaResponseDTO> dtos = documentoPropostaService.salvarDocumentos(propostaId, files);
        return ResponseEntity.ok(dtos);
    }
}
