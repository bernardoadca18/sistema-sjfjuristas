package com.sjfjuristas.plataforma.backend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "\"many_Administradores_handle_many_PagamentosParcela\"", schema = "schema_sjfjuristas")
public class ManyAdministradoresHandleManyPagamentosparcela {
    @EmbeddedId
    private ManyAdministradoresHandleManyPagamentosparcelaId id;

    @MapsId("adminstradorIdAdministradores")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "\"adminstrador_id_Administradores\"", nullable = false)
    private Administrador adminstradorIdAdministradores;

    @MapsId("pagamentoIdPagamentosparcela")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "\"pagamento_id_PagamentosParcela\"", nullable = false)
    private PagamentoParcela pagamentoIdPagamentosparcela;

}