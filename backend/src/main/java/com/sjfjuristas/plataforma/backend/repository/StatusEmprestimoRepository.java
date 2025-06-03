package com.sjfjuristas.plataforma.backend.repository;

import com.sjfjuristas.plataforma.backend.domain.StatusEmprestimo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StatusEmprestimoRepository extends JpaRepository<StatusEmprestimo, UUID> {
    Optional<StatusEmprestimo> findByNomeStatus(String nomeStatus);
}