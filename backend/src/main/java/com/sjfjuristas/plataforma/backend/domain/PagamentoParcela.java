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

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "\"PagamentosParcela\"", schema = "schema_sjfjuristas", uniqueConstraints = {
        @UniqueConstraint(name = "PagamentosParcela_id_transacao_pagamento_psp_uq", columnNames = {"id_transacao_pagamento_psp"})
})
public class PagamentoParcela {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "pagamento_id", nullable = false)
    private UUID id;

    @NotNull
    @Column(name = "valor_pago", nullable = false, precision = 16, scale = 2)
    private BigDecimal valorPago;

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "data_pagamento_efetivo", nullable = false)
    private OffsetDateTime dataPagamentoEfetivo;

    @Size(max = 50)
    @ColumnDefault("'PIX'")
    @Column(name = "meio_pagamento", length = 50)
    private String meioPagamento;

    @Size(max = 255)
    @Column(name = "id_transacao_pagamento_psp")
    private String idTransacaoPagamentoPsp;

    @Column(name = "data_confirmacao_manual")
    private OffsetDateTime dataConfirmacaoManual;

    @Column(name = "webhook_payload_psp")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> webhookPayloadPsp;

    @Column(name = "observacoes", length = Integer.MAX_VALUE)
    private String observacoes;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "\"usuario_id_Usuarios\"")
    private Usuario usuarioIdUsuarios;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "\"emprestimo_id_Emprestimos\"")
    private Emprestimo emprestimoIdEmprestimos;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "\"parcela_id_ParcelasEmprestimo\"")
    private ParcelaEmprestimo parcelaIdParcelasemprestimo;

    @OneToMany(mappedBy = "pagamentoIdPagamentosparcela")
    private Set<ComprovantePagamento> comprovantesPagamentos = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "pagamentosParcelas")
    private Set<Administrador> administradores = new LinkedHashSet<>();

}