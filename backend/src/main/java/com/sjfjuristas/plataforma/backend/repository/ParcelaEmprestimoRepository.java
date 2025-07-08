package com.sjfjuristas.plataforma.backend.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sjfjuristas.plataforma.backend.domain.Emprestimo;
import com.sjfjuristas.plataforma.backend.domain.ParcelaEmprestimo;

@Repository
public interface ParcelaEmprestimoRepository extends JpaRepository<ParcelaEmprestimo, UUID> {

    List<ParcelaEmprestimo> findByEmprestimoIdEmprestimosOrderByNumeroParcelaAsc(Emprestimo emprestimo);
    Page<ParcelaEmprestimo> findByEmprestimoIdEmprestimosOrderByNumeroParcelaAsc(Emprestimo emprestimo, Pageable pageable);
    Optional<ParcelaEmprestimo> findByEmprestimoIdEmprestimosAndNumeroParcela(Emprestimo emprestimo, Integer numeroParcela);
    
    @Query(value = """
        SELECT p.* FROM schema_sjfjuristas.parcelas_emprestimo p
        JOIN schema_sjfjuristas.status_pagamento_parcela s ON p.status_pagamento_parcela_id_status_pagamento_parcela = s.status_pagamento_parcela_id
        WHERE p.emprestimo_id_emprestimos = :emprestimoId AND s.nome_status = :statusNome
        ORDER BY p.data_vencimento ASC
        LIMIT 1
        """, nativeQuery=true)
    Optional<ParcelaEmprestimo> findProximaParcelaPendente(UUID emprestimoId, String statusNome);

    @Query(value = """
        SELECT p.numeroParcela FROM ParcelaEmprestimo p
        WHERE p.emprestimoIdEmprestimos = :emprestimo
        AND p.statusPagamentoParcelaIdStatuspagamentoparcela.nomeStatus NOT IN ('Pago', 'Pago com Atraso')
        ORDER BY p.numeroParcela ASC
        LIMIT 1
        """)
    Optional<Integer> findNumeroDaPrimeiraParcelaNaoPaga(@Param("emprestimo") Emprestimo emprestimo);

    @Query(value = """
        SELECT p FROM ParcelaEmprestimo p
        WHERE p.emprestimoIdEmprestimos = :emprestimo
        AND p.numeroParcela IN :numeros
        ORDER BY p.numeroParcela ASC
        """)
    List<ParcelaEmprestimo> findByEmprestimoAndNumeroParcelaIn(@Param("emprestimo") Emprestimo emprestimo, @Param("numeros") List<Integer> numeros);

    long countByEmprestimoIdEmprestimos(Emprestimo emprestimo);
}
