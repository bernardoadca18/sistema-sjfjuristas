package com.sjfjuristas.plataforma.backend.dto.Ocupacao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OcupacaoResponseDTO
{
    private UUID id;
    private String nome;
}