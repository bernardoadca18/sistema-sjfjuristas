package com.sjfjuristas.plataforma.backend.repository;

import com.sjfjuristas.plataforma.backend.domain.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, UUID>
{
    Optional<Administrador> findByEmail(String email);

    Optional<Administrador> findByMatriculaFuncionario(String matricula);

    List<Administrador> findByAtivo(boolean ativo);

    List<Administrador> findByCargoInterno(String cargoInterno);

    List<Administrador> findByDepartamento(String departamento);

    Optional<Administrador> findByTokenRecuperacaoSenha(String token);

    boolean existsByEmail(String email);

    boolean existsByMatriculaFuncionario(String matricula);
}
