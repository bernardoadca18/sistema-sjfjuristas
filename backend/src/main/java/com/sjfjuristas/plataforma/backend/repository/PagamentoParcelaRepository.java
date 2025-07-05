package com.sjfjuristas.plataforma.backend.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sjfjuristas.plataforma.backend.domain.Emprestimo;
import com.sjfjuristas.plataforma.backend.domain.PagamentoParcela;
import com.sjfjuristas.plataforma.backend.domain.ParcelaEmprestimo;

@Repository
public interface PagamentoParcelaRepository extends JpaRepository<PagamentoParcela, UUID> {

    List<PagamentoParcela> findByParcelaIdParcelasemprestimo(ParcelaEmprestimo parcela);

    List<PagamentoParcela> findByEmprestimoIdEmprestimos(Emprestimo emprestimo);

    Optional<PagamentoParcela> findByIdTransacaoPagamentoPsp(String idTransacaoPsp);

    Page<PagamentoParcela> findByEmprestimoIdEmprestimos(Emprestimo emprestimo, Pageable pageable);
}