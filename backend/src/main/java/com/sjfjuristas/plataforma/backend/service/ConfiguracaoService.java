package com.sjfjuristas.plataforma.backend.service;

import com.sjfjuristas.plataforma.backend.domain.ConfiguracaoSistema;
import com.sjfjuristas.plataforma.backend.dto.ConfiguracaoSistema.ConfiguracaoRequestDTO;
import com.sjfjuristas.plataforma.backend.repository.ConfiguracaoSistemaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Service
public class ConfiguracaoService
{
    @Autowired
    private ConfiguracaoSistemaRepository configuracaoSistemaRepository;
    
    public static final String CHAVE_TAXA_JUROS_DIARIA = "TAXA_JUROS_DIARIA_PADRAO";

    @Cacheable(value = "configuracoes", key = "#chave")
    public ConfiguracaoSistema getConfigByChave(String chave)
    {
        return configuracaoSistemaRepository.findByChaveConfig(chave).orElseThrow(() -> new EntityNotFoundException("Configuração '" + chave + "' não encontrada."));
    }

    @Cacheable(value = "configuracoes", key = "'" + CHAVE_TAXA_JUROS_DIARIA + "'")
    public BigDecimal getTaxaJurosDiaria()
    {
        System.out.println("Buscando a taxa de juros do BANCO DE DADOS (cache miss)...");
        ConfiguracaoSistema config = configuracaoSistemaRepository.findByChaveConfig(CHAVE_TAXA_JUROS_DIARIA).orElseThrow(() -> new IllegalStateException("Configuração '" + CHAVE_TAXA_JUROS_DIARIA + "' não encontrada no banco."));

        return new BigDecimal(config.getValorConfig());
    }

    public Page<ConfiguracaoSistema> getAllConfigsPag(Pageable pageable)
    {
        return configuracaoSistemaRepository.findAll(pageable);
    }

    public ConfiguracaoSistema createConfiguracao(ConfiguracaoRequestDTO dto) 
    {
        if (configuracaoSistemaRepository.findByChaveConfig(dto.getChaveConfig()).isPresent())
        {
            throw new IllegalArgumentException("Configuração com a chave '" + dto.getChaveConfig() + "' já existe.");
        }

        ConfiguracaoSistema novaConfig = new ConfiguracaoSistema();
        novaConfig.setChaveConfig(dto.getChaveConfig());
        novaConfig.setValorConfig(dto.getValorConfig());
        novaConfig.setTipoDado(dto.getTipoDado());
        novaConfig.setDescricao(dto.getDescricao());
        novaConfig.setGrupoConfig(dto.getGrupoConfig());
        novaConfig.setDataModificacao(OffsetDateTime.now());
        return configuracaoSistemaRepository.save(novaConfig);
    }

    @CachePut(value = "configuracoes", key = "#chave")
    public ConfiguracaoSistema updateConfiguracao(String chave, ConfiguracaoRequestDTO dto)
    {
        ConfiguracaoSistema configExistente = getConfigByChave(chave);
        
        configExistente.setValorConfig(dto.getValorConfig());
        configExistente.setTipoDado(dto.getTipoDado());
        configExistente.setDescricao(dto.getDescricao());
        configExistente.setGrupoConfig(dto.getGrupoConfig());
        configExistente.setDataModificacao(OffsetDateTime.now());
        
        return configuracaoSistemaRepository.save(configExistente);
    }

    @CacheEvict(value = "configuracoes", key = "#chave")
    public void deleteConfiguracao(String chave) 
    {
        ConfiguracaoSistema config = getConfigByChave(chave);
        configuracaoSistemaRepository.delete(config);
    }
}
