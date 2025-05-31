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
public class ManyAdministradoresHandleManyConfiguracoessistemaId implements Serializable {
    private static final long serialVersionUID = -3892056061048634489L;
    @NotNull
    @Column(name = "\"adminstrador_id_Administradores\"", nullable = false)
    private UUID adminstradorIdAdministradores;

    @NotNull
    @Column(name = "\"config_id_ConfiguracoesSistema\"", nullable = false)
    private UUID configIdConfiguracoessistema;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ManyAdministradoresHandleManyConfiguracoessistemaId entity = (ManyAdministradoresHandleManyConfiguracoessistemaId) o;
        return Objects.equals(this.adminstradorIdAdministradores, entity.adminstradorIdAdministradores) &&
                Objects.equals(this.configIdConfiguracoessistema, entity.configIdConfiguracoessistema);
    }

    @Override
    public int hashCode() {
        return Objects.hash(adminstradorIdAdministradores, configIdConfiguracoessistema);
    }

}