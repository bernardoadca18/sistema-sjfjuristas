package com.sjfjuristas.plataforma.backend.repository;

import com.sjfjuristas.plataforma.backend.domain.PropostaHistorico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

@Repository
public interface PropostaHistoricoRepository extends JpaRepository<PropostaHistorico, UUID>
{
    Page<PropostaHistorico> findByPropostaIdOrderByDataAlteracaoDesc(UUID propostaId, Pageable pageable);
}