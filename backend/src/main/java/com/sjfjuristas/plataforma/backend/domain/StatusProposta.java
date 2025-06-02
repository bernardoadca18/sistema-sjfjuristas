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
@Table(name = "\"StatusProposta\"", schema = "schema_sjfjuristas", uniqueConstraints = {
        @UniqueConstraint(name = "statusproposta_nome_status_uq", columnNames = {"nome_status"})
})
public class StatusProposta {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "status_proposta_id", nullable = false)
    private UUID id;

    @Size(max = 50)
    @NotNull
    @Column(name = "nome_status", nullable = false, length = 50)
    private String nomeStatus;

    @OneToMany(mappedBy = "statusPropostaIdStatusproposta")
    private Set<PropostaEmprestimo> propostasEmprestimos = new LinkedHashSet<>();

}