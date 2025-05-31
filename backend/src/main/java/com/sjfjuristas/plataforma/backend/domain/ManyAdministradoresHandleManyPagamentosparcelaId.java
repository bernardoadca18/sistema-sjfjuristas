package com.sjfjuristas.plataforma.backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Embeddable
public class ManyAdministradoresHandleManyPagamentosparcelaId implements Serializable {
    private static final long serialVersionUID = -7952568255157748816L;
    @NotNull
    @Column(name = "\"adminstrador_id_Administradores\"", nullable = false)
    private UUID adminstradorIdAdministradores;

    @NotNull
    @Column(name = "\"pagamento_id_PagamentosParcela\"", nullable = false)
    private UUID pagamentoIdPagamentosparcela;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ManyAdministradoresHandleManyPagamentosparcelaId entity = (ManyAdministradoresHandleManyPagamentosparcelaId) o;
        return Objects.equals(this.adminstradorIdAdministradores, entity.adminstradorIdAdministradores) &&
                Objects.equals(this.pagamentoIdPagamentosparcela, entity.pagamentoIdPagamentosparcela);
    }

    @Override
    public int hashCode() {
        return Objects.hash(adminstradorIdAdministradores, pagamentoIdPagamentosparcela);
    }

}