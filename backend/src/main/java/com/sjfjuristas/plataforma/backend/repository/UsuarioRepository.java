package com.sjfjuristas.plataforma.backend.repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sjfjuristas.plataforma.backend.domain.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, UUID>, JpaSpecificationExecutor<Usuario>
{
    Optional<Usuario> findByCpf(String cpf);
    
    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByEmailAndDataNascimento(String email, LocalDate dataNascimento);

    Optional<Usuario> findByTokenVerificacaoEmail(String token);

    Optional<Usuario> findByTokenRecuperacaoSenha(String token);

    @Query("SELECT u FROM Usuario u JOIN FETCH u.perfilIdPerfisusuario WHERE u.email = :email")
    Optional<Usuario> findByEmailWithPerfil(@Param("email") String email);

    boolean existsByEmail(String email);

    boolean existsByCpf(String cpf);

    Page<Usuario> findByNomeCompletoContainingIgnoreCase(String nomeCompleto, Pageable pageable);

    @Query(
        value = "SELECT u.* FROM schema_sjfjuristas.usuarios u " +
                "JOIN schema_sjfjuristas.propostas_emprestimo p ON u.usuario_id = p.usuario_id_usuarios " +
                "WHERE p.proposta_id = :propostaId",
        nativeQuery = true
    )
    Optional<Usuario> findUsuarioByPropostaId(@Param("propostaId") UUID propostaId);

}