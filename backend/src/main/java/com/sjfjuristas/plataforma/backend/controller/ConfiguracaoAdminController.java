package com.sjfjuristas.plataforma.backend.controller;

import com.sjfjuristas.plataforma.backend.domain.ConfiguracaoSistema;
import com.sjfjuristas.plataforma.backend.dto.ConfiguracaoSistema.ConfiguracaoRequestDTO;
import com.sjfjuristas.plataforma.backend.dto.ConfiguracaoSistema.ConfiguracaoResponseDTO;
import com.sjfjuristas.plataforma.backend.service.ConfiguracaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/configuracoes")
// TODO: Adicionar segurança para permitir acesso apenas a administradores
public class ConfiguracaoAdminController 
{
    @Autowired
    private ConfiguracaoService configuracaoService;

    @GetMapping
    public ResponseEntity<Page<ConfiguracaoResponseDTO>> listarConfiguracoes(@PageableDefault(page = 0, size = 10, sort = "chaveConfig") Pageable pageable)
    {   
        Page<ConfiguracaoSistema> paginaDeEntidades = configuracaoService.getAllConfigsPag(pageable);
        
        Page<ConfiguracaoResponseDTO> paginaDeDTOs = paginaDeEntidades.map(ConfiguracaoResponseDTO::new);
        
        return ResponseEntity.ok(paginaDeDTOs);
    }

    @GetMapping("/{chave}")
    public ResponseEntity<ConfiguracaoResponseDTO> obterConfiguracaoPorChave(@PathVariable String chave)
    {
        ConfiguracaoResponseDTO dto = new ConfiguracaoResponseDTO(configuracaoService.getConfigByChave(chave));
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<ConfiguracaoResponseDTO> criarConfiguracao(@Valid @RequestBody ConfiguracaoRequestDTO dto)
    {
        ConfiguracaoResponseDTO responseDto = new ConfiguracaoResponseDTO(configuracaoService.createConfiguracao(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PutMapping("/{chave}")
    public ResponseEntity<ConfiguracaoResponseDTO> atualizarConfiguracao(@PathVariable String chave, @Valid @RequestBody ConfiguracaoRequestDTO dto)
    {
        ConfiguracaoResponseDTO responseDto = new ConfiguracaoResponseDTO(configuracaoService.updateConfiguracao(chave, dto));
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{chave}")
    public ResponseEntity<Void> deletarConfiguracao(@PathVariable String chave)
    {
        configuracaoService.deleteConfiguracao(chave);
        return ResponseEntity.noContent().build();
    }
}
