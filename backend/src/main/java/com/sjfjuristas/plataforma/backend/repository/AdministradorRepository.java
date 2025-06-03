package com.sjfjuristas.plataforma.backend.repository;

import com.sjfjuristas.plataforma.backend.domain.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, UUID> {

    Optional<Administrador> findByEmail(String email);

    Optional<Administrador> findByMatriculaFuncionario(String matricula);

    boolean existsByEmail(String email);

    boolean existsByMatriculaFuncionario(String matricula);
}
