package com.sjfjuristas.plataforma.backend.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "\"ParcelasEmprestimo\"", schema = "schema_sjfjuristas")
public class ParcelaEmprestimo {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "parcela_id", nullable = false)
    private UUID id;

    @NotNull
    @Column(name = "numero_parcela", nullable = false)
    private Integer numeroParcela;

    @NotNull
    @Column(name = "data_vencimento", nullable = false)
    private LocalDate dataVencimento;

    @NotNull
    @Column(name = "valor_principal_amortizado", nullable = false, precision = 16, scale = 2)
    private BigDecimal valorPrincipalAmortizado;

    @NotNull
    @Column(name = "valor_juros", nullable = false, precision = 16, scale = 2)
    private BigDecimal valorJuros;

    @NotNull
    @Column(name = "valor_total_parcela", nullable = false, precision = 16, scale = 2)
    private BigDecimal valorTotalParcela;

    @Column(name = "pix_copia_cola", length = Integer.MAX_VALUE)
    private String pixCopiaCola;

    @Column(name = "pix_qr_code_base64", length = Integer.MAX_VALUE)
    private String pixQrCodeBase64;

    @Column(name = "data_geracao_pix")
    private OffsetDateTime dataGeracaoPix;

    @Column(name = "parcela_paga")
    private Boolean parcelaPaga = false;

    @Size(max = 255)
    @Column(name = "id_transacao_geracao_pix_psp")
    private String idTransacaoGeracaoPixPsp;

    @Column(name = "pix_data_expiracao")
    private OffsetDateTime pixDataExpiracao;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "\"status_pagamento_parcela_id_StatusPagamentoParcela\"")
    private StatusPagamentoParcela statusPagamentoParcelaIdStatuspagamentoparcela;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "\"emprestimo_id_Emprestimos\"")
    private Emprestimo emprestimoIdEmprestimos;

    @OneToMany(mappedBy = "parcelaIdParcelasemprestimo")
    private Set<ComprovantePagamento> comprovantesPagamentos = new LinkedHashSet<>();

    @OneToMany(mappedBy = "parcelaIdParcelasemprestimo")
    private Set<PagamentoParcela> pagamentosParcelas = new LinkedHashSet<>();

}