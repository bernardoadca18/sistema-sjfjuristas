package com.sjfjuristas.plataforma.backend.dto.Usuario;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
import java.time.LocalDate;
import lombok.AllArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PreCadastroCheckDTO
{
    private String cpf;
    private String email;
    private LocalDate dataNascimento;
}
