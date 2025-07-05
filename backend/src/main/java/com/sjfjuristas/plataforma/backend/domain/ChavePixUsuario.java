package com.sjfjuristas.plataforma.backend.domain;

import java.time.OffsetDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.ColumnDefault;
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
@Table(name = "\"ChavesPixUsuario\"", schema = "schema_sjfjuristas")
public class ChavePixUsuario {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "chave_pix_id", nullable = false)
    private UUID id;

    @Size(max = 255)
    @NotNull
    @Column(name = "valor_chave", nullable = false)
    private String valorChave;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "data_cadastro")
    private OffsetDateTime dataCadastro = OffsetDateTime.now();

    @ColumnDefault("false")
    @Column(name = "ativa_para_desembolso")
    private Boolean ativaParaDesembolso;

    @ColumnDefault("false")
    @Column(name = "verificada")
    private Boolean verificada = false;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "data_verificacao")
    private OffsetDateTime dataVerificacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "\"tipo_chave_pix_id_TiposChavePix\"")
    private TipoChavePix tipoChavePixIdTiposchavepix;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "\"usuario_id_Usuarios\"")
    private Usuario usuarioIdUsuarios;

    @OneToMany(mappedBy = "chavePixIdChavespixusuario")
    private Set<Emprestimo> emprestimos = new LinkedHashSet<>();

}