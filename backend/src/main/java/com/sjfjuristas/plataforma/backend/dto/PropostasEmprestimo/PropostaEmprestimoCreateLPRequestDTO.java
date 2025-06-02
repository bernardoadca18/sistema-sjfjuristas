package com.sjfjuristas.plataforma.backend.dto.PropostasEmprestimo;

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
    private BigDecimal valorSolicitado;
    private String nomeCompletoSolicitante;
    private String cpfSolicitante;
    private String emailSolicitante;
    private String telefoneWhatsappSolicitante;
    private Boolean termosAceitosLp;
}
