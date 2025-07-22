package com.sjfjuristas.plataforma.backend.domain;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ocupacoes", schema = "schema_sjfjuristas")
public class Ocupacao
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ocupacao_id", nullable = false)
    private UUID id;

    @Size(max = 255)
    @NotNull
    @Column(name = "nome_ocupacao", nullable = false, unique = true)
    private String nomeOcupacao;

    @Column(name = "ativo")
    private Boolean ativo = true;

    @ManyToMany(mappedBy = "ocupacoes")
    @JsonIgnore
    private Set<Usuario> usuarios = new LinkedHashSet<>();
}
