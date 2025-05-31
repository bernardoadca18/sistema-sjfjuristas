package com.sjfjuristas.plataforma.backend.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "\"DocumentosProposta\"", schema = "s.sjfjuristas")
public class DocumentoProposta {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "documento_id", nullable = false)
    private UUID id;

    @NotNull
    @Column(name = "url_documento", nullable = false, length = Integer.MAX_VALUE)
    private String urlDocumento;

    @Size(max = 255)
    @Column(name = "nome_arquivo_original")
    private String nomeArquivoOriginal;

    @Size(max = 100)
    @Column(name = "tipo_mime", length = 100)
    private String tipoMime;

    @Column(name = "tamanho_bytes")
    private Long tamanhoBytes;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "data_upload")
    private OffsetDateTime dataUpload;

    @Size(max = 255)
    @Column(name = "hash_documento")
    private String hashDocumento;

    @Size(max = 50)
    @ColumnDefault("'Pendente'")
    @Column(name = "status_validacao", length = 50)
    private String statusValidacao;

    @Column(name = "data_validacao")
    private OffsetDateTime dataValidacao;

    @Column(name = "observacoes_validacao", length = Integer.MAX_VALUE)
    private String observacoesValidacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "\"tipo_documento_id_TiposDocumento\"")
    private TipoDocumento tipoDocumentoIdTiposdocumento;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "\"usuario_id_Usuarios\"")
    private Usuario usuarioIdUsuarios;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "\"proposta_id_PropostasEmprestimo\"")
    private PropostaEmprestimo propostaIdPropostasemprestimo;

}