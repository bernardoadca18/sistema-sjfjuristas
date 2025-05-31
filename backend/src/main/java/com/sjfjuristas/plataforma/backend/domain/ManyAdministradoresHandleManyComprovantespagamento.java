package com.sjfjuristas.plataforma.backend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "\"many_Administradores_handle_many_ComprovantesPagamento\"", schema = "s.sjfjuristas")
public class ManyAdministradoresHandleManyComprovantespagamento {
    @EmbeddedId
    private ManyAdministradoresHandleManyComprovantespagamentoId id;

    @MapsId("adminstradorIdAdministradores")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "\"adminstrador_id_Administradores\"", nullable = false)
    private Administrador adminstradorIdAdministradores;

    @MapsId("comprovanteIdComprovantespagamento")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "\"comprovante_id_ComprovantesPagamento\"", nullable = false)
    private ComprovantePagamento comprovanteIdComprovantespagamento;

}