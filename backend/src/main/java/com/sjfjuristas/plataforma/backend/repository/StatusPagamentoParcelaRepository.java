package com.sjfjuristas.plataforma.backend.repository;

import com.sjfjuristas.plataforma.backend.domain.StatusPagamentoParcela;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StatusPagamentoParcelaRepository extends JpaRepository<StatusPagamentoParcela, UUID> 
{
    Optional<StatusPagamentoParcela> findByNomeStatus(String nomeStatus);
}