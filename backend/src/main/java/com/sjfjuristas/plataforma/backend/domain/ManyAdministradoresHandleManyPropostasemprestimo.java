package com.sjfjuristas.plataforma.backend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "\"many_Administradores_handle_many_PropostasEmprestimo\"", schema = "schema_sjfjuristas")
public class ManyAdministradoresHandleManyPropostasemprestimo {
    @EmbeddedId
    private ManyAdministradoresHandleManyPropostasemprestimoId id;

    @MapsId("adminstradorIdAdministradores")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "\"adminstrador_id_Administradores\"", nullable = false)
    private Administrador adminstradorIdAdministradores;

    @MapsId("propostaIdPropostasemprestimo")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "\"proposta_id_PropostasEmprestimo\"", nullable = false)
    private PropostaEmprestimo propostaIdPropostasemprestimo;

}