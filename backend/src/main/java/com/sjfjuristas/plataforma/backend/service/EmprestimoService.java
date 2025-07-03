package com.sjfjuristas.plataforma.backend.service;

import com.sjfjuristas.plataforma.backend.domain.*;
import com.sjfjuristas.plataforma.backend.repository.*;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sjfjuristas.plataforma.backend.dto.Emprestimos.CondicoesAprovadasDTO;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.sjfjuristas.plataforma.backend.dto.Emprestimos.EmprestimoClienteResponseDTO;
import com.sjfjuristas.plataforma.backend.dto.ParcelaEmprestimo.ParcelaEmprestimoResponseDTO;

@Service
public class EmprestimoService {
    @Autowired
    private EmprestimoRepository emprestimoRepository;

    @Autowired
    private ParcelaEmprestimoRepository parcelaRepository;

    @Autowired
    private StatusPagamentoParcelaRepository statusPagamentoParcelaRepository;

    @Autowired
    private StatusEmprestimoRepository statusEmprestimoRepository;

    @Autowired
    private PropostaEmprestimoRepository propostaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    //@Autowired
    //private ConfiguracaoService configuracaoService;

    @Transactional
    public Emprestimo criarEmprestimoEGerarParcelas(UUID propostaId, CondicoesAprovadasDTO condicoes) 
    {
        BigDecimal taxaJurosDiariaPercentual =  condicoes.getTaxaJurosDiaria();
        BigDecimal taxaJurosDiariaDecimal = taxaJurosDiariaPercentual.divide(new BigDecimal("100"), 8, RoundingMode.HALF_UP);
        
        PropostaEmprestimo proposta = propostaRepository.findById(propostaId)
                .orElseThrow(() -> new IllegalArgumentException("Proposta não encontrada."));
        
        Usuario usuario = proposta.getUsuarioIdUsuarios();

        StatusEmprestimo statusInicialEmprestimo = statusEmprestimoRepository.findByNomeStatus("Pendente Desembolso").orElseThrow(() -> new IllegalStateException("Status 'Pendente Desembolso' não encontrado."));

        BigDecimal valorParcelaDiaria = calcularValorParcela(
            condicoes.getValorContratado(),
            taxaJurosDiariaDecimal,
            condicoes.getNumeroTotalParcelas()
        );

        Emprestimo novoEmprestimo = new Emprestimo();
        novoEmprestimo.setPropostaIdPropostasemprestimo(proposta);
        novoEmprestimo.setUsuarioIdUsuarios(usuario);
        novoEmprestimo.setStatusEmprestimoIdStatusemprestimo(statusInicialEmprestimo);
        
        novoEmprestimo.setValorContratado(condicoes.getValorContratado());
        novoEmprestimo.setValorLiberado(condicoes.getValorLiberado());

        BigDecimal taxaJurosMensal = taxaJurosDiariaDecimal.max(new BigDecimal("30")).setScale(4, RoundingMode.HALF_UP);
        novoEmprestimo.setTaxaJurosMensalEfetiva(taxaJurosMensal);
        
        novoEmprestimo.setTaxaJurosDiariaEfetiva(taxaJurosDiariaPercentual);
        novoEmprestimo.setNumeroTotalParcelas(condicoes.getNumeroTotalParcelas());
        novoEmprestimo.setDataPrimeiroVencimento(condicoes.getDataPrimeiroVencimento());
        novoEmprestimo.setDataInicioCobrancaParcelas(condicoes.getDataPrimeiroVencimento());
        novoEmprestimo.setSaldoDevedorAtual(condicoes.getValorContratado());
        novoEmprestimo.setValorParcelaDiaria(valorParcelaDiaria);
        novoEmprestimo.setDataContratacao(OffsetDateTime.now());
        
        LocalDate ultimoVencimento = condicoes.getDataPrimeiroVencimento().plusDays(condicoes.getNumeroTotalParcelas() - 1);
        novoEmprestimo.setDataUltimoVencimento(ultimoVencimento);

        Emprestimo emprestimoSalvo = emprestimoRepository.save(novoEmprestimo);

        gerarEGravarParcelasPara(emprestimoSalvo, taxaJurosDiariaDecimal);

        return emprestimoSalvo;
    }

    @Transactional
    private void gerarEGravarParcelasPara(Emprestimo emprestimo, BigDecimal taxaJurosDiariaDecimal)
    {
        List<ParcelaEmprestimo> parcelasAGravar = new ArrayList<>();
        StatusPagamentoParcela statusPendente = statusPagamentoParcelaRepository
                .findByNomeStatus("Pendente")
                .orElseThrow(() -> new IllegalStateException("Status de parcela 'Pendente' não encontrado."));

        BigDecimal saldoDevedor = emprestimo.getValorContratado();
        BigDecimal valorParcela = emprestimo.getValorParcelaDiaria();

        for (int i = 1; i <= emprestimo.getNumeroTotalParcelas(); i++) {
            BigDecimal jurosDaParcela = saldoDevedor.multiply(taxaJurosDiariaDecimal).setScale(2, RoundingMode.HALF_UP);
            BigDecimal amortizacao = valorParcela.subtract(jurosDaParcela);

            if (amortizacao.compareTo(BigDecimal.ZERO) < 0 || i == emprestimo.getNumeroTotalParcelas())
            {
                amortizacao = saldoDevedor;
                valorParcela = saldoDevedor.add(jurosDaParcela);
            }

            ParcelaEmprestimo p = new ParcelaEmprestimo();
            p.setEmprestimoIdEmprestimos(emprestimo);
            p.setNumeroParcela(i);
            p.setDataVencimento(emprestimo.getDataPrimeiroVencimento().plusDays(i - 1));
            p.setValorTotalParcela(valorParcela);
            p.setValorJuros(jurosDaParcela);
            p.setValorPrincipalAmortizado(amortizacao);
            p.setStatusPagamentoParcelaIdStatuspagamentoparcela(statusPendente);

            parcelasAGravar.add(p);
            saldoDevedor = saldoDevedor.subtract(amortizacao);
        }

        parcelaRepository.saveAll(parcelasAGravar);
    }

    @Transactional
    private BigDecimal calcularValorParcela(BigDecimal valorPrincipal, BigDecimal taxaJurosDiaria, int numParcelas)
    {
        if (taxaJurosDiaria.compareTo(BigDecimal.ZERO) == 0) {
            return valorPrincipal.divide(new BigDecimal(numParcelas), 2, RoundingMode.HALF_UP);
        }
        BigDecimal i = taxaJurosDiaria;
        BigDecimal umMaisI = BigDecimal.ONE.add(i);
        BigDecimal umMaisIPotN = umMaisI.pow(numParcelas);
        BigDecimal numerador = i.multiply(umMaisIPotN);
        BigDecimal denominador = umMaisIPotN.subtract(BigDecimal.ONE);
        return valorPrincipal.multiply(numerador).divide(denominador, 2, RoundingMode.HALF_UP);
    }

    @Transactional(readOnly = true)
    public Page<EmprestimoClienteResponseDTO> getEmprestimosDoCliente(UUID clienteId, Pageable pageable)
    {
        Usuario usuario = usuarioRepository.findById(clienteId).orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        Page<Emprestimo> emprestimosPage = emprestimoRepository.findByUsuarioIdUsuarios(usuario, pageable);
        
        return emprestimosPage.map(EmprestimoClienteResponseDTO::new);
    }

    @Transactional(readOnly = true)
    public EmprestimoClienteResponseDTO getEmprestimoDoCliente(UUID emprestimoId, UUID clienteId)
    {
        Emprestimo emprestimo = emprestimoRepository.findById(emprestimoId)
                .orElseThrow(() -> new EntityNotFoundException("Empréstimo não encontrado."));
        
        if (!emprestimo.getUsuarioIdUsuarios().getId().equals(clienteId)) {
            throw new org.springframework.security.access.AccessDeniedException("Acesso negado a este empréstimo.");
        }
        
        return new EmprestimoClienteResponseDTO(emprestimo);
    }

    @Transactional(readOnly = true)
    public Page<ParcelaEmprestimoResponseDTO> getParcelasDoEmprestimo(UUID emprestimoId, UUID clienteId, Pageable pageable)
    {
        Emprestimo emprestimo = emprestimoRepository.findById(emprestimoId)
                .orElseThrow(() -> new EntityNotFoundException("Empréstimo não encontrado."));

        if (!emprestimo.getUsuarioIdUsuarios().getId().equals(clienteId)) {
            throw new org.springframework.security.access.AccessDeniedException("Acesso negado a este empréstimo.");
        }
        

        Page<ParcelaEmprestimo> parcelas = parcelaRepository.findByEmprestimoIdEmprestimosOrderByNumeroParcelaAsc(emprestimo, pageable);
        
        return parcelas.map(ParcelaEmprestimoResponseDTO::new);
    }
}
