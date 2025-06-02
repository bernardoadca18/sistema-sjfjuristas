package com.sjfjuristas.plataforma.backend.dto.ChavesPixUsuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChavePixCreateRequestDTO {
    private Long tipoChavePixId;
    private String valorChave;
}