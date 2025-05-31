package com.sjfjuristas.plataforma.backend.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "\"Emprestimos\"", schema = "s.sjfjuristas", indexes = {
        @Index(name = "emprestimos_proposta_id_uq", columnList = "proposta_id_PropostasEmprestimo", unique = true)
}, uniqueConstraints = {
        @UniqueConstraint(name = "emprestimo_id_transacao_desembolso_psp_uq", columnNames = {"id_transacao_desembolso_psp"}),
        @UniqueConstraint(name = "Emprestimos_uq", columnNames = {"proposta_id_PropostasEmprestimo"})
})
public class Emprestimo {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "emprestimo_id", nullable = false)
    private UUID id;

    @NotNull
    @Column(name = "valor_contratado", nullable = false, precision = 16, scale = 2)
    private BigDecimal valorContratado;

    @NotNull
    @Column(name = "valor_liberado", nullable = false, precision = 16, scale = 2)
    private BigDecimal valorLiberado;

    @NotNull
    @Column(name = "taxa_juros_mensal_efetiva", nullable = false, precision = 16, scale = 2)
    private BigDecimal taxaJurosMensalEfetiva;

    @NotNull
    @Column(name = "taxa_juros_diaria_efetiva", nullable = false, precision = 16, scale = 2)
    private BigDecimal taxaJurosDiariaEfetiva;

    @Column(name = "cet_anual", precision = 16, scale = 6)
    private BigDecimal cetAnual;

    @NotNull
    @Column(name = "numero_total_parcelas", nullable = false)
    private Integer numeroTotalParcelas;

    @NotNull
    @Column(name = "valor_parcela_diaria", nullable = false, precision = 16, scale = 2)
    private BigDecimal valorParcelaDiaria;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "data_contratacao")
    private OffsetDateTime dataContratacao;

    @NotNull
    @Column(name = "data_primeiro_vencimento", nullable = false)
    private LocalDate dataPrimeiroVencimento;

    @NotNull
    @Column(name = "data_ultimo_vencimento", nullable = false)
    private LocalDate dataUltimoVencimento;

    @NotNull
    @Column(name = "saldo_devedor_atual", nullable = false, precision = 16, scale = 2)
    private BigDecimal saldoDevedorAtual;

    @Column(name = "data_solicitacao_desembolso")
    private OffsetDateTime dataSolicitacaoDesembolso;

    @Column(name = "data_desembolso_efetivo")
    private OffsetDateTime dataDesembolsoEfetivo;

    @Size(max = 255)
    @Column(name = "id_transacao_desembolso_psp")
    private String idTransacaoDesembolsoPsp;

    @ColumnDefault("0.00")
    @Column(name = "iof_valor", precision = 16, scale = 2)
    private BigDecimal iofValor;

    @ColumnDefault("0.00")
    @Column(name = "seguro_valor", precision = 16, scale = 2)
    private BigDecimal seguroValor;

    @ColumnDefault("0.00")
    @Column(name = "outras_taxas", precision = 16, scale = 2)
    private BigDecimal outrasTaxas;

    @NotNull
    @Column(name = "data_inicio_cobranca_parcelas", nullable = false)
    private LocalDate dataInicioCobrancaParcelas;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "\"status_emprestimo_id_StatusEmprestimo\"")
    private StatusEmprestimo statusEmprestimoIdStatusemprestimo;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "\"usuario_id_Usuarios\"")
    private Usuario usuarioIdUsuarios;

    @OneToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "\"proposta_id_PropostasEmprestimo\"")
    private PropostaEmprestimo propostaIdPropostasemprestimo;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "\"chave_pix_id_ChavesPixUsuario\"")
    private ChavePixUsuario chavePixIdChavespixusuario;

    @OneToMany(mappedBy = "emprestimoIdEmprestimos")
    private Set<PagamentoParcela> pagamentosParcelas = new LinkedHashSet<>();

    @OneToMany(mappedBy = "emprestimoIdEmprestimos")
    private Set<ParcelaEmprestimo> parcelasEmprestimos = new LinkedHashSet<>();

}