package com.sjfjuristas.plataforma.backend.repository;


import com.sjfjuristas.plataforma.backend.domain.TipoChavePix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TipoChavePixRepository extends JpaRepository<TipoChavePix, UUID> {
    Optional<TipoChavePix> findByNomeTipo(String nomeTipo);
}