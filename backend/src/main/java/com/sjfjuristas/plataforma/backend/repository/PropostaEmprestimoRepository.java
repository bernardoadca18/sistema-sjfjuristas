package com.sjfjuristas.plataforma.backend.repository;

import com.sjfjuristas.plataforma.backend.domain.PropostaEmprestimo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PropostaEmprestimoRepository extends JpaRepository<PropostaEmprestimo, UUID> 
{
    Optional<PropostaEmprestimo> findTopByUsuarioIdOrderByDataPropostaDesc(UUID usuarioId);
}