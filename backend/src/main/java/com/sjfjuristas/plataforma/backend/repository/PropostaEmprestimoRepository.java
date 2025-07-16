package com.sjfjuristas.plataforma.backend.repository;

import com.sjfjuristas.plataforma.backend.domain.PropostaEmprestimo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;
import java.util.List;


@Repository
public interface PropostaEmprestimoRepository extends JpaRepository<PropostaEmprestimo, UUID> 
{
    //Optional<PropostaEmprestimo> findTopByUsuarioIdOrderByDataPropostaDesc(UUID usuarioId);
    Optional<PropostaEmprestimo> findTopByUsuarioIdUsuarios_IdOrderByDataSolicitacaoDesc(UUID usuarioId);
    
    @Query("SELECT p FROM PropostaEmprestimo p WHERE p.usuarioIdUsuarios.id = :usuarioId")
    Page<PropostaEmprestimo> findPropostasByUserId(@Param("usuarioId") UUID usuarioId, Pageable pageable);

    @Query("SELECT p FROM PropostaEmprestimo p WHERE p.usuarioIdUsuarios.id = :usuarioId")
    List<PropostaEmprestimo> findPropostasByUserIdNonPaged(@Param("usuarioId") UUID usuarioId);

}