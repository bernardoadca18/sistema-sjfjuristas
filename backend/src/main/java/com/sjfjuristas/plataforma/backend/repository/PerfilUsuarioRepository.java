package com.sjfjuristas.plataforma.backend.repository;

import com.sjfjuristas.plataforma.backend.domain.PerfilUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PerfilUsuarioRepository extends JpaRepository<PerfilUsuario, UUID> 
{
    Optional<PerfilUsuario> findByNomePerfil(String nomePerfil);
}
