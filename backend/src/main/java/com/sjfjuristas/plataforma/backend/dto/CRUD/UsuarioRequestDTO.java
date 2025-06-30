package com.sjfjuristas.plataforma.backend.dto.CRUD;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRequestDTO
{
    private String nome;
    private String email;
    private String telefone;
    private String cpf;
    private LocalDate dataNascimento;
    private String nomeMae;
    private BigDecimal rendaMensal;
    private UUID ocupacaoId;
    private boolean ativo;
}
