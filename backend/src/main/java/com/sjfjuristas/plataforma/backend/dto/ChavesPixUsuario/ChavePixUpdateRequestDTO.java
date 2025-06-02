package com.sjfjuristas.plataforma.backend.dto.ChavesPixUsuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChavePixUpdateRequestDTO {

    private Boolean ativaParaDesembolso; // Opcional
}