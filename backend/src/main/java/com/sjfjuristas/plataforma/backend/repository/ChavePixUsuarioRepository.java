package com.sjfjuristas.plataforma.backend.repository;

import com.sjfjuristas.plataforma.backend.domain.ChavePixUsuario;
import com.sjfjuristas.plataforma.backend.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChavePixUsuarioRepository extends JpaRepository<ChavePixUsuario, UUID> 
{
    List<ChavePixUsuario> findByUsuarioIdUsuarios(Usuario usuario);

    Optional<ChavePixUsuario> findByValorChave(String valorChave);
}