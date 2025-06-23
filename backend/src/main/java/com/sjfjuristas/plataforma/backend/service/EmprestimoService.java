package com.sjfjuristas.plataforma.backend.service;

import com.sjfjuristas.plataforma.backend.domain.*;
import com.sjfjuristas.plataforma.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sjfjuristas.plataforma.backend.dto.Emprestimos.CondicoesAprovadasDTO;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        
        //BigDecimal taxaJurosDiaria = configuracaoService.getTaxaJurosDiaria();
        BigDecimal taxaJurosDiaria =  condicoes.getTaxaJurosDiaria();
        
        PropostaEmprestimo proposta = propostaRepository.findById(propostaId)
                .orElseThrow(() -> new IllegalArgumentException("Proposta não encontrada."));
        
        Usuario usuario = proposta.getUsuarioIdUsuarios();

        StatusEmprestimo statusInicialEmprestimo = statusEmprestimoRepository.findByNomeStatus("Pendente Desembolso").orElseThrow(() -> new IllegalStateException("Status 'Pendente Desembolso' não encontrado."));

        BigDecimal valorParcelaDiaria = calcularValorParcela(
            condicoes.getValorContratado(),
            taxaJurosDiaria,
            condicoes.getNumeroTotalParcelas()
        );

        Emprestimo novoEmprestimo = new Emprestimo();
        novoEmprestimo.setPropostaIdPropostasemprestimo(proposta);
        novoEmprestimo.setUsuarioIdUsuarios(usuario);
        novoEmprestimo.setStatusEmprestimoIdStatusemprestimo(statusInicialEmprestimo);
        
        novoEmprestimo.setValorContratado(condicoes.getValorContratado());
        novoEmprestimo.setValorLiberado(condicoes.getValorLiberado());
        novoEmprestimo.setTaxaJurosDiariaEfetiva(taxaJurosDiaria);
        novoEmprestimo.setNumeroTotalParcelas(condicoes.getNumeroTotalParcelas());
        novoEmprestimo.setDataPrimeiroVencimento(condicoes.getDataPrimeiroVencimento());
        novoEmprestimo.setDataInicioCobrancaParcelas(condicoes.getDataPrimeiroVencimento());
        novoEmprestimo.setSaldoDevedorAtual(condicoes.getValorContratado());
        novoEmprestimo.setValorParcelaDiaria(valorParcelaDiaria);
        
        LocalDate ultimoVencimento = condicoes.getDataPrimeiroVencimento().plusDays(condicoes.getNumeroTotalParcelas() - 1);
        novoEmprestimo.setDataUltimoVencimento(ultimoVencimento);

        Emprestimo emprestimoSalvo = emprestimoRepository.save(novoEmprestimo);

        gerarEGravarParcelasPara(emprestimoSalvo);

        return emprestimoSalvo;
    }

    private void gerarEGravarParcelasPara(Emprestimo emprestimo) {
        List<ParcelaEmprestimo> parcelasAGravar = new ArrayList<>();
        StatusPagamentoParcela statusPendente = statusPagamentoParcelaRepository
                .findByNomeStatus("Pendente")
                .orElseThrow(() -> new IllegalStateException("Status de parcela 'Pendente' não encontrado."));

        BigDecimal saldoDevedor = emprestimo.getValorContratado();
        BigDecimal valorParcela = emprestimo.getValorParcelaDiaria();

        for (int i = 1; i <= emprestimo.getNumeroTotalParcelas(); i++) {
            BigDecimal jurosDaParcela = saldoDevedor.multiply(emprestimo.getTaxaJurosDiariaEfetiva()).setScale(2, RoundingMode.HALF_UP);
            BigDecimal amortizacao = valorParcela.subtract(jurosDaParcela);

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

    private BigDecimal calcularValorParcela(BigDecimal valorPrincipal, BigDecimal taxaJurosDiaria, int numParcelas) {
        // Fórmula da Tabela Price (PMT)
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
}
