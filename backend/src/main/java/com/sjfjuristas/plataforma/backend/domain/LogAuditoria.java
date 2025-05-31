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
@Table(name = "\"LogAuditoria\"", schema = "s.sjfjuristas")
public class LogAuditoria {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "log_id", nullable = false)
    private UUID id;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "timestamp_evento")
    private OffsetDateTime timestampEvento;

    @Size(max = 50)
    @Column(name = "perfil_usuario_no_evento", length = 50)
    private String perfilUsuarioNoEvento;

    @Size(max = 100)
    @NotNull
    @Column(name = "acao_codigo", nullable = false, length = 100)
    private String acaoCodigo;

    @Column(name = "descricao_acao", length = Integer.MAX_VALUE)
    private String descricaoAcao;

    @Size(max = 100)
    @Column(name = "entidade_afetada", length = 100)
    private String entidadeAfetada;

    @Size(max = 255)
    @Column(name = "id_entidade_afetada")
    private String idEntidadeAfetada;

    @Column(name = "detalhes_antes")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> detalhesAntes;

    @Column(name = "detalhes_depois")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> detalhesDepois;

    @Size(max = 45)
    @Column(name = "ip_origem", length = 45)
    private String ipOrigem;

    @Column(name = "user_agent", length = Integer.MAX_VALUE)
    private String userAgent;

    @ColumnDefault("true")
    @Column(name = "sucesso")
    private Boolean sucesso;

    @Column(name = "mensagem_erro", length = Integer.MAX_VALUE)
    private String mensagemErro;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "\"usuario_id_Usuarios\"")
    private Usuario usuarioIdUsuarios;

}