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
@Table(name = "\"StatusPagamentoParcela\"", schema = "s.sjfjuristas", uniqueConstraints = {
        @UniqueConstraint(name = "StatusPagamentoParcela_nome_status_uq", columnNames = {"nome_status"})
})
public class StatusPagamentoParcela {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "status_pagamento_parcela_id", nullable = false)
    private UUID id;

    @Size(max = 50)
    @NotNull
    @Column(name = "nome_status", nullable = false, length = 50)
    private String nomeStatus;

    @OneToMany(mappedBy = "statusPagamentoParcelaIdStatuspagamentoparcela")
    private Set<ParcelaEmprestimo> parcelasEmprestimos = new LinkedHashSet<>();

}