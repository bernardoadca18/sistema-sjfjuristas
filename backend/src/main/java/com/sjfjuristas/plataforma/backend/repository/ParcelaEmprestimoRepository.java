package com.sjfjuristas.plataforma.backend.repository;

import com.sjfjuristas.plataforma.backend.domain.ParcelaEmprestimo;
import com.sjfjuristas.plataforma.backend.domain.Emprestimo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ParcelaEmprestimoRepository extends JpaRepository<ParcelaEmprestimo, UUID> {

    List<ParcelaEmprestimo> findByEmprestimoIdEmprestimosOrderByNumeroParcelaAsc(Emprestimo emprestimo);
    Page<ParcelaEmprestimo> findByEmprestimoIdEmprestimosOrderByNumeroParcelaAsc(Emprestimo emprestimo, Pageable pageable);
    Optional<ParcelaEmprestimo> findByEmprestimoIdEmprestimosAndNumeroParcela(Emprestimo emprestimo, Integer numeroParcela); // Ajuste
}
