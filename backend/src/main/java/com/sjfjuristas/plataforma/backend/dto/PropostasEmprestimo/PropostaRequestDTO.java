package com.sjfjuristas.plataforma.backend.dto.PropostasEmprestimo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PropostaRequestDTO {

    @NotNull(message = "O valor solicitado é obrigatório.")
    @Positive(message = "O valor solicitado deve ser positivo.")
    private BigDecimal valorSolicitado;

    @NotBlank(message = "O nome completo é obrigatório.")
    private String nomeCompleto;

    @NotBlank(message = "O CPF é obrigatório.")
    private String cpf;

    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "O e-mail deve ser válido.")
    private String email;

    @NotBlank(message = "O WhatsApp é obrigatório.")
    private String whatsapp;

    @NotNull(message = "A aceitação dos termos é obrigatória.")
    private Boolean termosAceitos;
}