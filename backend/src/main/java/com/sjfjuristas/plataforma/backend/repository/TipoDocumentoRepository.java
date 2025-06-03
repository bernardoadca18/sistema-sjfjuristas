package com.sjfjuristas.plataforma.backend.repository;

import com.sjfjuristas.plataforma.backend.domain.TipoDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TipoDocumentoRepository extends JpaRepository<TipoDocumento, UUID> {
    Optional<TipoDocumento> findByNomeDocumento(String nomeDocumento);
}
