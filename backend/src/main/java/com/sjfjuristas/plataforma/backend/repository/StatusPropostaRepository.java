package com.sjfjuristas.plataforma.backend.repository;

import com.sjfjuristas.plataforma.backend.domain.StatusProposta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StatusPropostaRepository extends JpaRepository<StatusProposta, UUID> {
    Optional<StatusProposta> findByNomeStatus(String nomeStatus);
}