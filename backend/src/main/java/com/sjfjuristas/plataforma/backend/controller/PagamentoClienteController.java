package com.sjfjuristas.plataforma.backend.controller;

import com.sjfjuristas.plataforma.backend.domain.Usuario;
import com.sjfjuristas.plataforma.backend.dto.ParcelaEmprestimo.ParcelaEmprestimoResponseDTO;
import com.sjfjuristas.plataforma.backend.service.ParcelaEmprestimoService;
import com.sjfjuristas.plataforma.backend.service.PagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/cliente/pagamentos")
public class PagamentoClienteController
{
    @Autowired
    private ParcelaEmprestimoService parcelaService;
    
    @Autowired
    private PagamentoService pagamentoService;

    @PostMapping("/parcelas/{parcelaId}/gerar-pix")
    public ResponseEntity<ParcelaEmprestimoResponseDTO> gerarPixParaPagamento(@AuthenticationPrincipal Usuario usuarioLogado, @PathVariable UUID parcelaId)
    {    
        ParcelaEmprestimoResponseDTO dto = parcelaService.gerarPixParaParcela(parcelaId, usuarioLogado.getId());
        return ResponseEntity.ok(dto);
    }
    
    @PostMapping("/parcelas/{parcelaId}/anexar-comprovante")
    public ResponseEntity<Void> anexarComprovante(@AuthenticationPrincipal Usuario usuarioLogado, @PathVariable UUID parcelaId, @RequestParam("comprovante") MultipartFile arquivo) throws IOException
    {    
        pagamentoService.anexarComprovante(parcelaId, usuarioLogado.getId(), arquivo);
        return ResponseEntity.ok().build();
    }
}
