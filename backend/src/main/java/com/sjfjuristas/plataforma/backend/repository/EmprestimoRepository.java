package com.sjfjuristas.plataforma.backend.repository;

import com.sjfjuristas.plataforma.backend.domain.Emprestimo;
import com.sjfjuristas.plataforma.backend.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmprestimoRepository extends JpaRepository<Emprestimo, UUID> {

    Optional<Emprestimo> findByPropostaIdPropostasEmprestimo_Id(UUID propostaId);

    List<Emprestimo> findByUsuarioIdUsuarios(Usuario usuario);
}
