package com.sjfjuristas.plataforma.backend.dto.Emprestimos;

import com.sjfjuristas.plataforma.backend.domain.Emprestimo;
import com.sjfjuristas.plataforma.backend.domain.ParcelaEmprestimo;
import com.sjfjuristas.plataforma.backend.dto.ParcelaEmprestimo.ParcelaEmprestimoSummaryDTO;
import com.sjfjuristas.plataforma.backend.dto.Usuario.ClienteResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmprestimoAdminResponseDTO
{
    // Campos do ClienteResponseDTO
    private UUID id;
    private BigDecimal valorContratado;
    private BigDecimal valorLiberado;
    private BigDecimal taxaJurosDiariaEfetiva;
    private Integer numeroTotalParcelas;
    private BigDecimal valorParcelaDiaria;
    private OffsetDateTime dataContratacao;
    private LocalDate dataPrimeiroVencimento;
    private LocalDate dataUltimoVencimento;
    private String statusEmprestimoNome;
    private BigDecimal saldoDevedorAtual;
    private LocalDate dataInicioCobrancaParcelas;
    private ParcelaEmprestimoSummaryDTO proximaParcela; // Opcional

    // Campos adicionais do Admin
    private ClienteResponseDTO usuarioCliente;
    private UUID propostaId;
    private String chavePixDesembolsoInfo;
    private OffsetDateTime dataDesembolsoEfetivo; // Opcional
    private String idTransacaoDesembolsoPsp; // Opcional


    public EmprestimoAdminResponseDTO(Emprestimo entity)
    {
        this.id = entity.getId();
        this.valorContratado = entity.getValorContratado();
        this.valorLiberado = entity.getValorLiberado();
        this.taxaJurosDiariaEfetiva = entity.getTaxaJurosDiariaEfetiva();
        this.numeroTotalParcelas = entity.getNumeroTotalParcelas();
        this.valorParcelaDiaria = entity.getValorParcelaDiaria();
        this.dataContratacao = entity.getDataContratacao();
        this.dataPrimeiroVencimento = entity.getDataPrimeiroVencimento();
        this.dataUltimoVencimento = entity.getDataUltimoVencimento();
        this.saldoDevedorAtual = entity.getSaldoDevedorAtual();
        this.dataInicioCobrancaParcelas = entity.getDataInicioCobrancaParcelas();
        this.dataDesembolsoEfetivo = entity.getDataDesembolsoEfetivo();
        this.idTransacaoDesembolsoPsp = entity.getIdTransacaoDesembolsoPsp();
        
        if (entity.getStatusEmprestimoIdStatusemprestimo() != null)
        {
            this.statusEmprestimoNome = entity.getStatusEmprestimoIdStatusemprestimo().getNomeStatus();
        }

        if (entity.getUsuarioIdUsuarios() != null)
        {
            this.usuarioCliente = new ClienteResponseDTO(entity.getUsuarioIdUsuarios());
        }

        if (entity.getPropostaIdPropostasemprestimo() != null)
        {
            this.propostaId = entity.getPropostaIdPropostasemprestimo().getId();
        }
        
        if (entity.getChavePixIdChavespixusuario() != null)
        {
            this.chavePixDesembolsoInfo = entity.getChavePixIdChavespixusuario().getTipoChavePixIdTiposchavepix().getNomeTipo() + ": " + entity.getChavePixIdChavespixusuario().getValorChave();
        }
        
        if (entity.getParcelasEmprestimos() != null && !entity.getParcelasEmprestimos().isEmpty())
        {
            this.proximaParcela = entity.getParcelasEmprestimos().stream()
            .filter(p -> p.getStatusPagamentoParcelaIdStatuspagamentoparcela() != null && "Pendente".equals(p.getStatusPagamentoParcelaIdStatuspagamentoparcela().getNomeStatus()))
            .min(Comparator.comparing(ParcelaEmprestimo::getDataVencimento)).map(ParcelaEmprestimoSummaryDTO::new).orElse(null);
        }
    }
}