package com.sjfjuristas.plataforma.backend.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "\"TiposDocumento\"", schema = "schema_sjfjuristas", uniqueConstraints = {
        @UniqueConstraint(name = "TiposDocumento_nome_documento_uq", columnNames = {"nome_documento"})
})
public class TipoDocumento {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "tipo_documento_id", nullable = false)
    private UUID id;

    @Size(max = 100)
    @NotNull
    @Column(name = "nome_documento", nullable = false, length = 100)
    private String nomeDocumento;

    @ColumnDefault("true")
    @Column(name = "obrigatorio_proposta")
    private Boolean obrigatorioProposta;

    @Column(name = "descricao", length = Integer.MAX_VALUE)
    private String descricao;

    @OneToMany(mappedBy = "tipoDocumentoIdTiposdocumento")
    private Set<DocumentoProposta> documentosPropostas = new LinkedHashSet<>();

}