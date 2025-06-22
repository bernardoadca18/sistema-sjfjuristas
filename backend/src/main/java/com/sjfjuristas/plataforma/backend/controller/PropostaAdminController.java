package com.sjfjuristas.plataforma.backend.controller;

import com.sjfjuristas.plataforma.backend.domain.Emprestimo;
import com.sjfjuristas.plataforma.backend.domain.PropostaEmprestimo;
import com.sjfjuristas.plataforma.backend.dto.Emprestimos.CondicoesAprovadasDTO;
import com.sjfjuristas.plataforma.backend.dto.Emprestimos.EmprestimoAdminResponseDTO;
import com.sjfjuristas.plataforma.backend.dto.PropostasEmprestimo.ContrapropostaAdminRequestDTO;
import com.sjfjuristas.plataforma.backend.dto.PropostasEmprestimo.PropostaResponseDTO;
import com.sjfjuristas.plataforma.backend.service.EmprestimoService;
import com.sjfjuristas.plataforma.backend.service.PropostaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/propostas")
// TODO: Proteger esta rota para ser acessível apenas por administradores
public class PropostaAdminController
{
    @Autowired
    private PropostaService propostaService;

    @Autowired
    private EmprestimoService emprestimoService;


    @PostMapping("/{propostaId}/aprovar")
    public ResponseEntity<EmprestimoAdminResponseDTO> aprovarProposta(@PathVariable UUID propostaId, @Valid @RequestBody CondicoesAprovadasDTO condicoes)
    {    
        propostaService.atualizarStatus(propostaId, "Aprovada");

        Emprestimo emprestimoCriado = emprestimoService.criarEmprestimoEGerarParcelas(propostaId, condicoes);
        

        // EmprestimoAdminResponseDTO response = ...
        
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
