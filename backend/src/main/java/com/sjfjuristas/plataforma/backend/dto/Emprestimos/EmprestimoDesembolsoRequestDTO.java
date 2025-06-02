package com.sjfjuristas.plataforma.backend.dto.Emprestimos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmprestimoDesembolsoRequestDTO {
    private UUID chavePixDesembolsoId;
    private BigDecimal valorLiberar;
}