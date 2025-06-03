package com.sjfjuristas.plataforma.backend.repository;

import com.sjfjuristas.plataforma.backend.domain.DocumentoProposta;
import com.sjfjuristas.plataforma.backend.domain.PropostaEmprestimo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface DocumentoPropostaRepository extends JpaRepository<DocumentoProposta, UUID> {

    List<DocumentoProposta> findByPropostaIdPropostasEmprestimo(PropostaEmprestimo proposta);
}
