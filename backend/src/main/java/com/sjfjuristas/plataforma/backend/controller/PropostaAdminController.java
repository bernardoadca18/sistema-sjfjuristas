package com.sjfjuristas.plataforma.backend.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sjfjuristas.plataforma.backend.domain.Emprestimo;
import com.sjfjuristas.plataforma.backend.domain.PropostaEmprestimo;
import com.sjfjuristas.plataforma.backend.dto.Emprestimos.CondicoesAprovadasDTO;
import com.sjfjuristas.plataforma.backend.dto.Emprestimos.EmprestimoAdminResponseDTO;
import com.sjfjuristas.plataforma.backend.dto.PropostasEmprestimo.ContrapropostaAdminRequestDTO;
import com.sjfjuristas.plataforma.backend.dto.PropostasEmprestimo.PropostaHistoricoResponseDTO;
import com.sjfjuristas.plataforma.backend.dto.PropostasEmprestimo.PropostaResponseDTO;
import com.sjfjuristas.plataforma.backend.service.EmprestimoService;
import com.sjfjuristas.plataforma.backend.service.PropostaService;

import jakarta.validation.Valid;

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
        

        EmprestimoAdminResponseDTO response = new EmprestimoAdminResponseDTO(emprestimoCriado);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{propostaId}/contraproposta")
    public ResponseEntity<PropostaResponseDTO> enviarContrapropostaAdmin(@PathVariable UUID propostaId, @Valid @RequestBody ContrapropostaAdminRequestDTO dto)
    {
        PropostaEmprestimo propostaAtualizada = propostaService.enviarContrapropostaAdmin(propostaId, dto);
        PropostaResponseDTO response = new PropostaResponseDTO(
            propostaAtualizada.getId(),
            propostaAtualizada.getValorOfertado(),
            propostaAtualizada.getNomeCompletoSolicitante(),
            propostaAtualizada.getEmailSolicitante(),
            propostaAtualizada.getDataSolicitacao(),
            propostaAtualizada.getStatusPropostaIdStatusproposta().getNomeStatus()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{propostaId}/negar")
    public ResponseEntity<Void> negarProposta(@PathVariable UUID propostaId, @RequestBody String motivo)
    {
        propostaService.negarPropostaAdmin(propostaId, motivo);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{propostaId}/historico")
    public ResponseEntity<Page<PropostaHistoricoResponseDTO>> getHistoricoPropostaAdmin(@PathVariable UUID propostaId, @PageableDefault(page = 0, size = 10, sort = "dataAlteracao", direction = Sort.Direction.DESC) Pageable pageable)
    {
        Page<PropostaHistoricoResponseDTO> historico = propostaService.getHistoricoProposta(propostaId, pageable);
        return ResponseEntity.ok(historico);
    }
}
