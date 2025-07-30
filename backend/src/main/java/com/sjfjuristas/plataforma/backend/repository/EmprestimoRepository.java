package com.sjfjuristas.plataforma.backend.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sjfjuristas.plataforma.backend.domain.Emprestimo;
import com.sjfjuristas.plataforma.backend.domain.StatusEmprestimo;
import com.sjfjuristas.plataforma.backend.domain.Usuario;

@Repository
public interface EmprestimoRepository extends JpaRepository<Emprestimo, UUID> {
    List<Emprestimo> findByUsuarioIdUsuarios(Usuario usuario);
    Page<Emprestimo> findByUsuarioIdUsuarios(Usuario usuario, Pageable pageable);
    List<Emprestimo> findByStatusEmprestimoIdStatusemprestimo(StatusEmprestimo statusEmprestimo);
    Optional<Emprestimo> findByPropostaIdPropostasemprestimo_Id(UUID id);

    @Query  (
                "SELECT e FROM Emprestimo e " +
                "LEFT JOIN FETCH e.statusEmprestimoIdStatusemprestimo " +
                "WHERE e.usuarioIdUsuarios.id = :usuarioId"
            )
    List<Emprestimo> findEmprestimosByUsuarioId(@Param("usuarioId") UUID usuarioId);

    @Query("SELECT e FROM Emprestimo e WHERE e.usuarioIdUsuarios.id = :usuarioId AND e.chavePixIdChavespixusuario IS NULL")
    List<Emprestimo> findEmprestimosSemChavePixDefinida(@Param("usuarioId") UUID usuarioId);
}
