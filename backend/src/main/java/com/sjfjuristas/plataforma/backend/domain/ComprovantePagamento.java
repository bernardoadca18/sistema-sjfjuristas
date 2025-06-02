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
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "\"ComprovantesPagamento\"", schema = "schema_sjfjuristas")
public class ComprovantePagamento {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "comprovante_id", nullable = false)
    private UUID id;

    @NotNull
    @Column(name = "url_comprovante", nullable = false, length = Integer.MAX_VALUE)
    private String urlComprovante;

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

    @Size(max = 50)
    @ColumnDefault("'Pendente'")
    @Column(name = "status_verificacao", length = 50)
    private String statusVerificacao;

    @Column(name = "data_verificacao_admin")
    private OffsetDateTime dataVerificacaoAdmin;

    @Column(name = "observacoes_verificacao", length = Integer.MAX_VALUE)
    private String observacoesVerificacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "\"usuario_id_Usuarios\"")
    private Usuario usuarioIdUsuarios;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "\"parcela_id_ParcelasEmprestimo\"")
    private ParcelaEmprestimo parcelaIdParcelasemprestimo;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "\"pagamento_id_PagamentosParcela\"")
    private PagamentoParcela pagamentoIdPagamentosparcela;

    @ManyToMany(mappedBy = "comprovantesPagamentos")
    private Set<Administrador> administradores = new LinkedHashSet<>();

}