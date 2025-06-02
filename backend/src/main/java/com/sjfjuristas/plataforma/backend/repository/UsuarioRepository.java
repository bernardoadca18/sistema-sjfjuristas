package com.sjfjuristas.plataforma.backend.repository;

import com.sjfjuristas.plataforma.backend.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, UUID> 
{
    Optional<Usuario> findByEmail(String email);


    Optional<Usuario> findByCpf(String cpf);


    Optional<Usuario> findByTokenVerificacaoEmail(String token);


    Optional<Usuario> findByTokenRecuperacaoSenha(String token);


    @Query("SELECT u FROM Usuario u JOIN FETCH u.perfilIdPerfisusuario WHERE u.email = :email")
    Optional<Usuario> findByEmailWithPerfil(@Param("email") String email);


    boolean existsByEmail(String email);


    boolean existsByCpf(String cpf);
}