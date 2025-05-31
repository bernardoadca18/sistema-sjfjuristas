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
public class ManyAdministradoresHandleManyPropostasemprestimoId implements Serializable {
    private static final long serialVersionUID = 3995109509641691456L;
    @NotNull
    @Column(name = "\"adminstrador_id_Administradores\"", nullable = false)
    private UUID adminstradorIdAdministradores;

    @NotNull
    @Column(name = "\"proposta_id_PropostasEmprestimo\"", nullable = false)
    private UUID propostaIdPropostasemprestimo;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ManyAdministradoresHandleManyPropostasemprestimoId entity = (ManyAdministradoresHandleManyPropostasemprestimoId) o;
        return Objects.equals(this.adminstradorIdAdministradores, entity.adminstradorIdAdministradores) &&
                Objects.equals(this.propostaIdPropostasemprestimo, entity.propostaIdPropostasemprestimo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(adminstradorIdAdministradores, propostaIdPropostasemprestimo);
    }

}