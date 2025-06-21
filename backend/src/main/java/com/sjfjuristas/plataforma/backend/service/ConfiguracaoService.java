package com.sjfjuristas.plataforma.backend.service;

import com.sjfjuristas.plataforma.backend.domain.ConfiguracaoSistema;
import com.sjfjuristas.plataforma.backend.repository.ConfiguracaoSistemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class ConfiguracaoService
{
    @Autowired
    private ConfiguracaoSistemaRepository configuracaoSistemaRepository;
    
    public static final String CHAVE_TAXA_JUROS_DIARIA = "TAXA_JUROS_DIARIA_PADRAO";

    @Cacheable(value = "configuracoes", key = "'" + CHAVE_TAXA_JUROS_DIARIA + "'")
    public BigDecimal getTaxaJurosDiaria()
    {
        System.out.println("Buscando a taxa de juros do BANCO DE DADOS (cache miss)...");
        ConfiguracaoSistema config = configuracaoSistemaRepository.findByChaveConfig(CHAVE_TAXA_JUROS_DIARIA).orElseThrow(() -> new IllegalStateException("Configuração '" + CHAVE_TAXA_JUROS_DIARIA + "' não encontrada no banco."));

        return new BigDecimal(config.getValorConfig());
    }
}
