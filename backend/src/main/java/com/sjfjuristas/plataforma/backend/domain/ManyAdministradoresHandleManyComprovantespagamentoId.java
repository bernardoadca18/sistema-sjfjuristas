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
public class ManyAdministradoresHandleManyComprovantespagamentoId implements Serializable {
    private static final long serialVersionUID = 4311508779557142984L;
    @NotNull
    @Column(name = "\"adminstrador_id_Administradores\"", nullable = false)
    private UUID adminstradorIdAdministradores;

    @NotNull
    @Column(name = "\"comprovante_id_ComprovantesPagamento\"", nullable = false)
    private UUID comprovanteIdComprovantespagamento;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ManyAdministradoresHandleManyComprovantespagamentoId entity = (ManyAdministradoresHandleManyComprovantespagamentoId) o;
        return Objects.equals(this.adminstradorIdAdministradores, entity.adminstradorIdAdministradores) &&
                Objects.equals(this.comprovanteIdComprovantespagamento, entity.comprovanteIdComprovantespagamento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(adminstradorIdAdministradores, comprovanteIdComprovantespagamento);
    }

}