package com.sjfjuristas.plataforma.backend.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "\"TiposChavePix\"", schema = "s.sjfjuristas", uniqueConstraints = {
        @UniqueConstraint(name = "pix_nome_tipo_uq", columnNames = {"nome_tipo"})
})
public class TipoChavePix {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "tipo_chave_pix_id", nullable = false)
    private UUID id;

    @Size(max = 50)
    @NotNull
    @Column(name = "nome_tipo", nullable = false, length = 50)
    private String nomeTipo;

    @OneToMany(mappedBy = "tipoChavePixIdTiposchavepix")
    private Set<ChavePixUsuario> chavesPixUsuarios = new LinkedHashSet<>();

}