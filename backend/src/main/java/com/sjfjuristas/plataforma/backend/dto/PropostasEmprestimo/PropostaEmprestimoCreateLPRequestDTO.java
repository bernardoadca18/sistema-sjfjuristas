package com.sjfjuristas.plataforma.backend.dto.PropostasEmprestimo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PropostaEmprestimoCreateLPRequestDTO 
{
    @NotNull(message = "O valor solicitado é obrigatório.")
    @Positive(message = "O valor solicitado deve ser positivo.")
    private BigDecimal valorSolicitado;

    @NotBlank(message = "O nome completo é obrigatório.")
    private String nomeCompletoSolicitante;

    @NotBlank(message = "O CPF é obrigatório.")
    private String cpfSolicitante;

    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "O formato do e-mail é inválido.")
    private String emailSolicitante;

    private String telefoneWhatsappSolicitante; // Opcional

    @NotNull(message = "É obrigatório aceitar os termos.")
    private Boolean termosAceitosLp;
}
