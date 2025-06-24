package com.sjfjuristas.plataforma.backend.repository;

import com.sjfjuristas.plataforma.backend.domain.Ocupacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface OcupacaoRepository extends JpaRepository<Ocupacao, UUID>
{
    List<Ocupacao> findByAtivoTrueOrderByNomeOcupacaoAsc();
}
