package com.sjfjuristas.plataforma.backend.repository;

import com.sjfjuristas.plataforma.backend.domain.Notificacao;
import com.sjfjuristas.plataforma.backend.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, UUID> {

    List<Notificacao> findByUsuarioIdUsuariosOrderByDataCriacaoDesc(Usuario usuario);

    List<Notificacao> findByUsuarioIdUsuariosAndLidaIsFalseOrderByDataCriacaoDesc(Usuario usuario);
}