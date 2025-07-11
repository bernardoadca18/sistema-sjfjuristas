package com.sjfjuristas.plataforma.backend.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sjfjuristas.plataforma.backend.domain.ChavePixUsuario;
import com.sjfjuristas.plataforma.backend.domain.Usuario;

@Repository
public interface ChavePixUsuarioRepository extends JpaRepository<ChavePixUsuario, UUID> 
{
    List<ChavePixUsuario> findByUsuarioIdUsuarios(Usuario usuario);

    Optional<ChavePixUsuario> findByValorChave(String valorChave);

    @Query(value = """
        SELECT * FROM schema_sjfjuristas.chaves_pix_usuario 
        WHERE usuario_id_usuarios = :usuarioId AND ativa_para_desembolso = true 
        LIMIT 1
        """, nativeQuery = true)
    Optional<ChavePixUsuario> findChavePixAtivaByUsuarioId(@Param("usuarioId") UUID usuarioId);
}