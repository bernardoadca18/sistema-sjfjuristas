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
@Table(name = "\"ChavesPixUsuario\"", schema = "s.sjfjuristas")
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
    private OffsetDateTime dataCadastro;

    @ColumnDefault("false")
    @Column(name = "ativa_para_desembolso")
    private Boolean ativaParaDesembolso;

    @ColumnDefault("false")
    @Column(name = "\"FALSE\"")
    private Boolean falseField;

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