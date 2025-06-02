package com.sjfjuristas.plataforma.backend.dto.ChavesPixUsuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChavePixResponseDTO {

    private UUID id;
    private String tipoChavePixNome;
    private String valorChaveMascarado;
    private boolean ativaParaDesembolso;
    private boolean verificada;
    private OffsetDateTime dataCadastro;
}