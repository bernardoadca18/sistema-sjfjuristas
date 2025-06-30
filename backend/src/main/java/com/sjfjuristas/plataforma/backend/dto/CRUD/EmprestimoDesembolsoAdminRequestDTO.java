package com.sjfjuristas.plataforma.backend.dto.CRUD;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
// Receber a confirmação de desembolso de um empréstimo pelo Administrador.
// POST /api/admin/emprestimos/{id}/registrar-desembolso
public class EmprestimoDesembolsoAdminRequestDTO
{
    private LocalDate dataDesembolso;
    private String observacao;
}
