package com.sjfjuristas.plataforma.backend.dto.PropostasEmprestimo;

import com.sjfjuristas.plataforma.backend.dto.DocumentoProposta.DocumentoPropostaSummaryDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PropostaEmprestimoResponseDTO {

    private UUID id;
    private BigDecimal valorSolicitado;
    private String nomeCompletoSolicitante;
    private String cpfSolicitante; // Pode ser parcialmente mascarado
    private String emailSolicitante;
    private String telefoneWhatsappSolicitante;
    private OffsetDateTime dataSolicitacao;
    private String statusPropostaNome;
    private String analisadoPorAdminNome; // Opcional
    private OffsetDateTime dataAnalise; // Opcional
    private String motivoNegacao; // Opcional
    private List<DocumentoPropostaSummaryDTO> documentos;
}
