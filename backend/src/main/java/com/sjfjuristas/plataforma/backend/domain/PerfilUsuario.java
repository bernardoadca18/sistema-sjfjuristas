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
@Table(name = "\"PerfisUsuario\"", schema = "schema_sjfjuristas", uniqueConstraints = {
        @UniqueConstraint(name = "PerfisUsuario_nome_perfil_uq", columnNames = {"nome_perfil"})
})
public class PerfilUsuario {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "perfil_id", nullable = false)
    private UUID id;

    @Size(max = 50)
    @NotNull
    @Column(name = "nome_perfil", nullable = false, length = 50)
    private String nomePerfil;

    @OneToMany(mappedBy = "perfilIdPerfisusuario")
    private Set<Usuario> usuarios = new LinkedHashSet<>();

}