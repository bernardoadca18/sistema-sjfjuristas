package com.sjfjuristas.plataforma.backend.repository;

import com.sjfjuristas.plataforma.backend.domain.PagamentoParcela;
import com.sjfjuristas.plataforma.backend.domain.ParcelaEmprestimo;
import com.sjfjuristas.plataforma.backend.domain.Emprestimo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PagamentoParcelaRepository extends JpaRepository<PagamentoParcela, UUID> {

    List<PagamentoParcela> findByParcelaIdParcelasEmprestimo(ParcelaEmprestimo parcela);

    List<PagamentoParcela> findByEmprestimoIdEmprestimos(Emprestimo emprestimo);

    Optional<PagamentoParcela> findByIdTransacaoPagamentoPsp(String idTransacaoPsp);
}