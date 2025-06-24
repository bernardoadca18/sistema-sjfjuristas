package com.sjfjuristas.plataforma.backend.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

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
}
