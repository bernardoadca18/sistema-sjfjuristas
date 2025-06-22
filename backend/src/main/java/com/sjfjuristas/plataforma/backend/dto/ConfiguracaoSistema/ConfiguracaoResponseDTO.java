package com.sjfjuristas.plataforma.backend.dto.ConfiguracaoSistema;

import com.sjfjuristas.plataforma.backend.domain.ConfiguracaoSistema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfiguracaoResponseDTO {

    private UUID id;
    private String chaveConfig;
    private String valorConfig;
    private String tipoDado;
    private String descricao;
    private String grupoConfig;
    private OffsetDateTime dataModificacao;

    // Mapear Entidade pra DTO
    public ConfiguracaoResponseDTO(ConfiguracaoSistema config) {
        this.id = config.getId();
        this.chaveConfig = config.getChaveConfig();
        this.valorConfig = config.getValorConfig();
        this.tipoDado = config.getTipoDado();
        this.descricao = config.getDescricao();
        this.grupoConfig = config.getGrupoConfig();
        this.dataModificacao = config.getDataModificacao();
    }
}