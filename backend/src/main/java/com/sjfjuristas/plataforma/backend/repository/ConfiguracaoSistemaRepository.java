package com.sjfjuristas.plataforma.backend.repository;

import com.sjfjuristas.plataforma.backend.domain.ConfiguracaoSistema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConfiguracaoSistemaRepository extends JpaRepository<ConfiguracaoSistema, UUID> {

    Optional<ConfiguracaoSistema> findByChaveConfig(String chaveConfig);
}