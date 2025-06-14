package com.sjfjuristas.plataforma.backend.repository;

import com.sjfjuristas.plataforma.backend.domain.Emprestimo;
import com.sjfjuristas.plataforma.backend.domain.StatusEmprestimo;
import com.sjfjuristas.plataforma.backend.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmprestimoRepository extends JpaRepository<Emprestimo, UUID> {
    List<Emprestimo> findByUsuarioIdUsuarios(Usuario usuario);
    List<Emprestimo> findByStatusEmprestimoIdStatusemprestimo(StatusEmprestimo statusEmprestimo);
    Optional<Emprestimo> findByPropostaIdPropostasemprestimo_Id(UUID id);
}
