package com.sjfjuristas.plataforma.backend.dto.Emprestimos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClienteSummaryDTO {
    private String nomeCompleto;
    private String cpf;
}