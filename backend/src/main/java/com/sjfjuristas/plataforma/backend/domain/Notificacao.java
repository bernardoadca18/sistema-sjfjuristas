package com.sjfjuristas.plataforma.backend.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "\"Notificacoes\"", schema = "schema_sjfjuristas")
public class Notificacao {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "notificacao_id", nullable = false)
    private UUID id;

    @Size(max = 255)
    @NotNull
    @Column(name = "titulo", nullable = false)
    private String titulo;

    @NotNull
    @Column(name = "mensagem", nullable = false, length = Integer.MAX_VALUE)
    private String mensagem;

    @Size(max = 50)
    @NotNull
    @Column(name = "tipo_notificacao", nullable = false, length = 50)
    private String tipoNotificacao;

    @Column(name = "metadados")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> metadados;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "data_criacao")
    private OffsetDateTime dataCriacao;

    @Column(name = "data_leitura")
    private OffsetDateTime dataLeitura;

    @ColumnDefault("false")
    @Column(name = "lida")
    private Boolean lida;

    @Column(name = "link_destino_app", length = Integer.MAX_VALUE)
    private String linkDestinoApp;

    @ColumnDefault("false")
    @Column(name = "enviada_por_push")
    private Boolean enviadaPorPush;

    @Column(name = "data_envio_email")
    private OffsetDateTime dataEnvioEmail;

    @Column(name = "data_envio_push")
    private OffsetDateTime dataEnvioPush;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "\"usuario_id_Usuarios\"")
    private Usuario usuarioIdUsuarios;

}