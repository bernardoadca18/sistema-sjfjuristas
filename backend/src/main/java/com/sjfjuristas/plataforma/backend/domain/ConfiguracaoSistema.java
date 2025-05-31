package com.sjfjuristas.plataforma.backend.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.OffsetDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "\"ConfiguracoesSistema\"", schema = "s.sjfjuristas", uniqueConstraints = {
        @UniqueConstraint(name = "ConfiguracoesSistema_chave_config_uq", columnNames = {"chave_config"})
})
public class ConfiguracaoSistema {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "config_id", nullable = false)
    private UUID id;

    @Size(max = 100)
    @NotNull
    @Column(name = "chave_config", nullable = false, length = 100)
    private String chaveConfig;

    @Column(name = "valor_config", length = Integer.MAX_VALUE)
    private String valorConfig;

    @Size(max = 50)
    @ColumnDefault("'TEXT'")
    @Column(name = "tipo_dado", length = 50)
    private String tipoDado;

    @Column(name = "descricao", length = Integer.MAX_VALUE)
    private String descricao;

    @Size(max = 50)
    @Column(name = "grupo_config", length = 50)
    private String grupoConfig;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "data_modificacao")
    private OffsetDateTime dataModificacao;

    @ManyToMany(mappedBy = "configuracoesSistemas")
    private Set<Administrador> administradores = new LinkedHashSet<>();

}