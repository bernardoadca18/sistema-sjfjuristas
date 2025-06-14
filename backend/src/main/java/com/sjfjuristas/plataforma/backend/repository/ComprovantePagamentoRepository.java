package com.sjfjuristas.plataforma.backend.repository;

import com.sjfjuristas.plataforma.backend.domain.ComprovantePagamento;
import com.sjfjuristas.plataforma.backend.domain.ParcelaEmprestimo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface ComprovantePagamentoRepository extends JpaRepository<ComprovantePagamento, UUID> 
{
    List<ComprovantePagamento> findByParcelaIdParcelasemprestimo(ParcelaEmprestimo parcela);
}