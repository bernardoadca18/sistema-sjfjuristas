package com.sjfjuristas.plataforma.backend.controller;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sjfjuristas.plataforma.backend.domain.Usuario;
import com.sjfjuristas.plataforma.backend.dto.PagamentosParcela.PagamentoParcelaResponseDTO;
import com.sjfjuristas.plataforma.backend.dto.ParcelaEmprestimo.ParcelaEmprestimoResponseDTO;
import com.sjfjuristas.plataforma.backend.service.PagamentoService;
import com.sjfjuristas.plataforma.backend.service.ParcelaEmprestimoService;

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

    @GetMapping("/emprestimo/{emprestimoId}/pagamentos")
    public ResponseEntity<Page<PagamentoParcelaResponseDTO>> getPagamentos(@PathVariable UUID emprestimoId, @PageableDefault(page = 0, size = 3, sort = "dataPagamentoEfetivo") Pageable pageable)
    {
        validarPropriedadeEmprestimo(emprestimoId);
        Page<PagamentoParcelaResponseDTO> pagamentos = pagamentoService.getPagamentosPorEmprestimo(emprestimoId, pageable);
        return ResponseEntity.ok(pagamentos);
    }

    private void validarPropriedadeEmprestimo(UUID emprestimoId)
    {
        pagamentoService.validarPropriedadeEmprestimo(emprestimoId);
    }
}
