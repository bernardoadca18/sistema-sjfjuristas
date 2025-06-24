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
@Table(name = "\"PropostasEmprestimo\"", schema = "schema_sjfjuristas")
public class PropostaEmprestimo {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "proposta_id", nullable = false)
    private UUID id;

    @NotNull
    @Column(name = "valor_solicitado", nullable = false, precision = 16, scale = 2)
    private BigDecimal valorSolicitado;

    @Size(max = 255)
    @NotNull
    @Column(name = "nome_completo_solicitante", nullable = false)
    private String nomeCompletoSolicitante;

    @Size(max = 14)
    @NotNull
    @Column(name = "cpf_solicitante", nullable = false, length = 14)
    private String cpfSolicitante;

    @Column(name = "data_nascimento_solicitante")
    private LocalDate dataNascimentoSolicitante;

    @Column(name = "num_parcelas_preferido")
    private Integer numParcelasPreferido;

    @Size(max = 255)
    @NotNull
    @Column(name = "email_solicitante", nullable = false)
    private String emailSolicitante;

    @Size(max = 20)
    @Column(name = "telefone_whatsapp_solicitante", length = 20)
    private String telefoneWhatsappSolicitante;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "data_solicitacao")
    private OffsetDateTime dataSolicitacao;

    @Size(max = 45)
    @Column(name = "ip_solicitacao", length = 45)
    private String ipSolicitacao;

    @Column(name = "user_agent_solicitacao", length = Integer.MAX_VALUE)
    private String userAgentSolicitacao;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "termos_aceitos_lp", nullable = false)
    private Boolean termosAceitosLp = false;

    @Column(name = "data_aceite_termos_lp")
    private OffsetDateTime dataAceiteTermosLp;

    @Column(name = "motivo_negacao", length = Integer.MAX_VALUE)
    private String motivoNegacao;

    @Column(name = "data_analise")
    private OffsetDateTime dataAnalise;

    @ColumnDefault("false")
    @Column(name = "link_app_enviado")
    private Boolean linkAppEnviado;

    @Column(name = "data_envio_link_app")
    private OffsetDateTime dataEnvioLinkApp;

    @Size(max = 100)
    @ColumnDefault("'LandingPage'")
    @Column(name = "origem_captacao", length = 100)
    private String origemCaptacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "\"status_proposta_id_StatusProposta\"")
    private StatusProposta statusPropostaIdStatusproposta;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "\"usuario_id_Usuarios\"")
    private Usuario usuarioIdUsuarios;

    @OneToMany(mappedBy = "propostaIdPropostasemprestimo")
    private Set<DocumentoProposta> documentosPropostas = new LinkedHashSet<>();

    @OneToOne(mappedBy = "propostaIdPropostasemprestimo")
    private Emprestimo emprestimo;

    @ManyToMany(mappedBy = "propostasEmprestimos")
    private Set<Administrador> administradores = new LinkedHashSet<>();

    @Column(name = "valor_ofertado", precision = 16, scale = 2)
    private BigDecimal valorOfertado;

    @Column(name = "taxa_juros_ofertada", precision = 16, scale = 4)
    private BigDecimal taxaJurosOfertada;

    @Column(name = "num_parcelas_ofertado")
    private Integer numParcelasOfertado;

    @Column(name = "data_deposito_prevista")
    private LocalDate dataDepositoPrevista;
    
    @Column(name = "data_inicio_pagamento_prevista")
    private LocalDate dataInicioPagamentoPrevista;

    @Size(max = 50)
    @Column(name = "origem_ultima_oferta", length = 50)
    private String origemUltimaOferta;
    
    @Column(name = "motivo_recusa_cliente", length = Integer.MAX_VALUE)
    private String motivoRecusaCliente;

    
    @Column(name = "remuneracao_mensal_solicitante", precision = 16, scale = 2)
    private BigDecimal remuneracaoMensalSolicitante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ocupacao_id_ocupacoes")
    private Ocupacao ocupacao;

    @Column(name = "outra_ocupacao_solicitante")
    private String outraOcupacaoSolicitante;

    @Column(name = "proposito_emprestimo")
    private String propositoEmprestimo;

    @Column(name = "estado_civil")
    private String estadoCivil;

    @Column(name = "possui_imovel_veiculo")
    private Boolean possuiImovelVeiculo;
}