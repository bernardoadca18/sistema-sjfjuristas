package com.sjfjuristas.plataforma.backend.repository;

import com.sjfjuristas.plataforma.backend.domain.PropostaEmprestimo;
import com.sjfjuristas.plataforma.backend.domain.Usuario;
import com.sjfjuristas.plataforma.backend.domain.StatusProposta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PropostaEmprestimoRepository extends JpaRepository<PropostaEmprestimo, UUID> {

    Optional<PropostaEmprestimo> findByCpfSolicitante(String cpfSolicitante);

    List<PropostaEmprestimo> findByUsuarioIdUsuarios(Usuario usuario);

    List<PropostaEmprestimo> findByStatusPropostaIdStatusProposta(StatusProposta statusProposta);
}
