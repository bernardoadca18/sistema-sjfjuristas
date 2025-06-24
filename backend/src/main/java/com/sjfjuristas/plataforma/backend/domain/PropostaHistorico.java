package com.sjfjuristas.plataforma.backend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "propostas_historico", schema = "schema_sjfjuristas")
public class PropostaHistorico
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "historico_id", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proposta_id_propostas_emprestimo", nullable = false)
    private PropostaEmprestimo proposta;

    @Column(name = "data_alteracao", nullable = false)
    private OffsetDateTime dataAlteracao = OffsetDateTime.now();

    @Column(name = "ator_alteracao", nullable = false)
    private String atorAlteracao;

    @Column(name = "status_anterior")
    private String statusAnterior;

    @Column(name = "status_novo")
    private String statusNovo;

    @Column(name = "valor_anterior", precision = 16, scale = 2)
    private BigDecimal valorAnterior;

    @Column(name = "valor_novo", precision = 16, scale = 2)
    private BigDecimal valorNovo;

    @Column(name = "num_parcelas_anterior")
    private Integer numParcelasAnterior;

    @Column(name = "num_parcelas_novo")
    private Integer numParcelasNovo;

    @Column(name = "taxa_juros_anterior", precision = 16, scale = 4)
    private BigDecimal taxaJurosAnterior;

    @Column(name = "taxa_juros_nova", precision = 16, scale = 4)
    private BigDecimal taxaJurosNova;

    @Column(name = "motivo_recusa", columnDefinition = "TEXT")
    private String motivoRecusa;

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;
}
