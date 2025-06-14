package com.sjfjuristas.plataforma.backend.controller;

import com.sjfjuristas.plataforma.backend.dto.PropostasEmprestimo.PropostaEmprestimoCreateLPRequestDTO;
import com.sjfjuristas.plataforma.backend.service.PropostaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal; // Importe a classe BigDecimal
import java.util.List;

@RestController
@RequestMapping("/api/propostas")
public class PropostaController {

    @Autowired
    private PropostaService propostaService;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<String> criarProposta(
            @RequestParam("valorSolicitado") BigDecimal valorSolicitado,
            @RequestParam("nomeCompletoSolicitante") String nomeCompletoSolicitante,
            @RequestParam("cpfSolicitante") String cpfSolicitante,
            @RequestParam("emailSolicitante") String emailSolicitante,
            @RequestParam("telefoneWhatsappSolicitante") String telefoneWhatsappSolicitante,
            @RequestParam("termosAceitosLp") Boolean termosAceitosLp,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) {

        PropostaEmprestimoCreateLPRequestDTO propostaDTO = new PropostaEmprestimoCreateLPRequestDTO();
        propostaDTO.setValorSolicitado(valorSolicitado);
        propostaDTO.setNomeCompletoSolicitante(nomeCompletoSolicitante);
        propostaDTO.setCpfSolicitante(cpfSolicitante);
        propostaDTO.setEmailSolicitante(emailSolicitante);
        propostaDTO.setTelefoneWhatsappSolicitante(telefoneWhatsappSolicitante);
        propostaDTO.setTermosAceitosLp(termosAceitosLp);

        try {
            propostaService.criarProposta(propostaDTO, files);
            return ResponseEntity.ok("Proposta recebida com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao receber proposta: " + e.getMessage());
        }
    }
}