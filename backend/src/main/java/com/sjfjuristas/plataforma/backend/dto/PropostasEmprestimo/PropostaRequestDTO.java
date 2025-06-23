package com.sjfjuristas.plataforma.backend.dto.PropostasEmprestimo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

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

    @NotNull(message = "A data de nascimento é obrigatória.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "ddMMyyyy")
    private LocalDate dataNascimento;

    @NotNull(message = "A preferência de parcelas é obrigatória.")
    @Min(value = 30, message = "O número mínimo de parcelas é 30.")
    @Max(value = 180, message = "O número máximo de parcelas é 180.")
    private Integer numParcelasPreferido;
}