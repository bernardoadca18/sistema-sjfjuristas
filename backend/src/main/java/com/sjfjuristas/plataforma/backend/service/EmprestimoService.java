package com.sjfjuristas.plataforma.backend.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sjfjuristas.plataforma.backend.domain.ChavePixUsuario;
import com.sjfjuristas.plataforma.backend.domain.Emprestimo;
import com.sjfjuristas.plataforma.backend.domain.ParcelaEmprestimo;
import com.sjfjuristas.plataforma.backend.domain.PropostaEmprestimo;
import com.sjfjuristas.plataforma.backend.domain.StatusEmprestimo;
import com.sjfjuristas.plataforma.backend.domain.StatusPagamentoParcela;
import com.sjfjuristas.plataforma.backend.domain.Usuario;
import com.sjfjuristas.plataforma.backend.dto.Emprestimos.CondicoesAprovadasDTO;
import com.sjfjuristas.plataforma.backend.dto.Emprestimos.EmprestimoClienteResponseDTO;
import com.sjfjuristas.plataforma.backend.dto.Emprestimos.EmprestimoSummaryDTO;
import com.sjfjuristas.plataforma.backend.dto.ParcelaEmprestimo.ParcelaEmprestimoResponseDTO;
import com.sjfjuristas.plataforma.backend.repository.ChavePixUsuarioRepository;
import com.sjfjuristas.plataforma.backend.repository.EmprestimoRepository;
import com.sjfjuristas.plataforma.backend.repository.ParcelaEmprestimoRepository;
import com.sjfjuristas.plataforma.backend.repository.PropostaEmprestimoRepository;
import com.sjfjuristas.plataforma.backend.repository.StatusEmprestimoRepository;
import com.sjfjuristas.plataforma.backend.repository.StatusPagamentoParcelaRepository;
import com.sjfjuristas.plataforma.backend.repository.UsuarioRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class EmprestimoService
{
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
    private ChavePixUsuarioRepository chavePixUsuarioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public Emprestimo criarEmprestimoEmAnalise(UUID propostaId)
    {
        PropostaEmprestimo proposta = propostaRepository.findById(propostaId).orElseThrow(() -> new IllegalArgumentException("Proposta não encontrada."));
        
        Usuario usuario = proposta.getUsuarioIdUsuarios();

        StatusEmprestimo statusInicialEmprestimo = statusEmprestimoRepository.findByNomeStatus("Em Análise").orElseThrow(() -> new IllegalStateException("Status 'Em Análise' não encontrado."));

        Emprestimo novoEmprestimo = new Emprestimo();

        novoEmprestimo.setPropostaIdPropostasemprestimo(proposta);
        novoEmprestimo.setUsuarioIdUsuarios(usuario);
        novoEmprestimo.setStatusEmprestimoIdStatusemprestimo(statusInicialEmprestimo);

        Emprestimo emprestimoSalvo = emprestimoRepository.save(novoEmprestimo);
        return emprestimoSalvo;
    }

    @Transactional
    public EmprestimoClienteResponseDTO findByPropostaId(UUID propostaId)
    {
        Emprestimo emprestimo = emprestimoRepository.findByPropostaIdPropostasemprestimo_Id(propostaId).orElseThrow(() -> new IllegalArgumentException("Empréstimo não encontrado"));
        return new EmprestimoClienteResponseDTO(emprestimo);
    }

    public void criarEmprestimosEmAnalise(List<UUID> propostaIds)
    {
        StatusEmprestimo statusInicialEmprestimo = statusEmprestimoRepository.findByNomeStatus("Em Análise").orElseThrow(() -> new IllegalStateException("Status 'Em Análise' não encontrado."));
        PropostaEmprestimo propostaSample = propostaRepository.findById(propostaIds.get(0)).orElseThrow(() -> new IllegalArgumentException("Proposta não encontrada."));
        Usuario usuario = propostaSample.getUsuarioIdUsuarios();
    
        List<PropostaEmprestimo> propostas = propostaRepository.findAllById(propostaIds);

        List<Emprestimo> emprestimosParaSalvar = propostas.stream().map(proposta -> {
            Emprestimo novoEmprestimo = new Emprestimo();

            ChavePixUsuario chavePixAtiva = chavePixUsuarioRepository.findChavePixAtivaByUsuarioId(usuario.getId()).get();
        
            if (chavePixAtiva != null && novoEmprestimo.getChavePixIdChavespixusuario() == null)
            {
                novoEmprestimo.setChavePixIdChavespixusuario(chavePixAtiva);
            }

            novoEmprestimo.setPropostaIdPropostasemprestimo(proposta);
            novoEmprestimo.setUsuarioIdUsuarios(usuario);
            novoEmprestimo.setStatusEmprestimoIdStatusemprestimo(statusInicialEmprestimo);

            return novoEmprestimo;
        })
        .collect(Collectors.toList());

        emprestimoRepository.saveAll(emprestimosParaSalvar);
    }

    @Transactional
    public Emprestimo aprovarEmprestimoEmAnalise(UUID propostaId, CondicoesAprovadasDTO condicoes)
    {
        Emprestimo novoEmprestimo = emprestimoRepository.findByPropostaIdPropostasemprestimo_Id(propostaId).orElseThrow(() -> new IllegalArgumentException("Empréstimo não encontrado."));

        BigDecimal taxaJurosDiariaPercentual =  condicoes.getTaxaJurosDiaria();
        BigDecimal taxaJurosDiariaDecimal = taxaJurosDiariaPercentual.divide(new BigDecimal("100"), 8, RoundingMode.HALF_UP);
        BigDecimal valorParcelaDiaria = calcularValorParcela( condicoes.getValorContratado(), taxaJurosDiariaDecimal, condicoes.getNumeroTotalParcelas() );
        BigDecimal taxaJurosMensal = taxaJurosDiariaDecimal.max(new BigDecimal("30")).setScale(4, RoundingMode.HALF_UP);
        
        LocalDate ultimoVencimento = condicoes.getDataPrimeiroVencimento().plusDays(condicoes.getNumeroTotalParcelas() - 1);
        
        ChavePixUsuario chavePixAtiva = chavePixUsuarioRepository.findChavePixAtivaByUsuarioId(novoEmprestimo.getUsuarioIdUsuarios().getId()).get();
        
        if (chavePixAtiva != null && novoEmprestimo.getChavePixIdChavespixusuario() == null)
        {
            novoEmprestimo.setChavePixIdChavespixusuario(chavePixAtiva);
        }

        novoEmprestimo.setValorContratado(condicoes.getValorContratado());
        novoEmprestimo.setValorLiberado(condicoes.getValorLiberado());
        novoEmprestimo.setTaxaJurosMensalEfetiva(taxaJurosMensal);
        novoEmprestimo.setTaxaJurosDiariaEfetiva(taxaJurosDiariaPercentual);
        novoEmprestimo.setNumeroTotalParcelas(condicoes.getNumeroTotalParcelas());
        novoEmprestimo.setDataPrimeiroVencimento(condicoes.getDataPrimeiroVencimento());
        novoEmprestimo.setDataInicioCobrancaParcelas(condicoes.getDataPrimeiroVencimento());
        novoEmprestimo.setSaldoDevedorAtual(condicoes.getValorContratado());
        novoEmprestimo.setValorParcelaDiaria(valorParcelaDiaria);
        novoEmprestimo.setDataContratacao(OffsetDateTime.now());
        novoEmprestimo.setDataUltimoVencimento(ultimoVencimento);

        Emprestimo emprestimoSalvo = emprestimoRepository.save(novoEmprestimo);

        gerarEGravarParcelasPara(emprestimoSalvo, taxaJurosDiariaDecimal);

        return emprestimoSalvo;
    }

    @Transactional
    public Emprestimo criarEmprestimoEGerarParcelas(UUID propostaId, CondicoesAprovadasDTO condicoes)
    {
        BigDecimal taxaJurosDiariaPercentual =  condicoes.getTaxaJurosDiaria();
        BigDecimal taxaJurosDiariaDecimal = taxaJurosDiariaPercentual.divide(new BigDecimal("100"), 8, RoundingMode.HALF_UP);
        BigDecimal valorParcelaDiaria = calcularValorParcela( condicoes.getValorContratado(), taxaJurosDiariaDecimal, condicoes.getNumeroTotalParcelas() );
        BigDecimal taxaJurosMensal = taxaJurosDiariaDecimal.max(new BigDecimal("30")).setScale(4, RoundingMode.HALF_UP);
        
        LocalDate ultimoVencimento = condicoes.getDataPrimeiroVencimento().plusDays(condicoes.getNumeroTotalParcelas() - 1);
        
        PropostaEmprestimo proposta = propostaRepository.findById(propostaId).orElseThrow(() -> new IllegalArgumentException("Proposta não encontrada."));
        Usuario usuario = proposta.getUsuarioIdUsuarios();
        StatusEmprestimo statusInicialEmprestimo = statusEmprestimoRepository.findByNomeStatus("Pendente Desembolso").orElseThrow(() -> new IllegalStateException("Status 'Pendente Desembolso' não encontrado."));

        
        Emprestimo novoEmprestimo = new Emprestimo();
        
        novoEmprestimo.setPropostaIdPropostasemprestimo(proposta);
        novoEmprestimo.setUsuarioIdUsuarios(usuario);
        novoEmprestimo.setStatusEmprestimoIdStatusemprestimo(statusInicialEmprestimo);
        
        ChavePixUsuario chavePixAtiva = chavePixUsuarioRepository.findChavePixAtivaByUsuarioId(usuario.getId()).orElseThrow(() -> new IllegalStateException("Chave Pix ativa não encontrada para o usuário."));
        
        if (chavePixAtiva != null && novoEmprestimo.getChavePixIdChavespixusuario() == null)
        {
            novoEmprestimo.setChavePixIdChavespixusuario(chavePixAtiva);
        }
        
        novoEmprestimo.setValorContratado(condicoes.getValorContratado());
        novoEmprestimo.setValorLiberado(condicoes.getValorLiberado());
        novoEmprestimo.setTaxaJurosMensalEfetiva(taxaJurosMensal);
        novoEmprestimo.setTaxaJurosDiariaEfetiva(taxaJurosDiariaPercentual);
        novoEmprestimo.setNumeroTotalParcelas(condicoes.getNumeroTotalParcelas());
        novoEmprestimo.setDataPrimeiroVencimento(condicoes.getDataPrimeiroVencimento());
        novoEmprestimo.setDataInicioCobrancaParcelas(condicoes.getDataPrimeiroVencimento());
        novoEmprestimo.setSaldoDevedorAtual(condicoes.getValorContratado());
        novoEmprestimo.setValorParcelaDiaria(valorParcelaDiaria);
        novoEmprestimo.setDataContratacao(OffsetDateTime.now());
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
        if (taxaJurosDiaria.compareTo(BigDecimal.ZERO) == 0)
        {
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
    public List<EmprestimoSummaryDTO> getInfoEmprestimosDoCliente(UUID clienteId)
    {
        Usuario usuario = usuarioRepository.findById(clienteId).orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        List<Emprestimo> emprestimosList = emprestimoRepository.findByUsuarioIdUsuarios(usuario);

        return emprestimosList.stream().map(EmprestimoSummaryDTO::new).toList();
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

    @Transactional
    public List<ParcelaEmprestimo> getParcelasParaWidget(UUID emprestimoId)
    {
        Emprestimo emprestimo = emprestimoRepository.findById(emprestimoId).orElseThrow(() -> new IllegalArgumentException("Empréstimo não encontrado."));

        Optional<Integer> numeroParcelaFocoOpt = parcelaRepository.findNumeroDaPrimeiraParcelaNaoPaga(emprestimo);

        if (numeroParcelaFocoOpt.isEmpty())
        {
            long totalParcelas = parcelaRepository.countByEmprestimoIdEmprestimos(emprestimo);
            List<Integer> numerosParaBuscar = List.of((int)totalParcelas - 2, (int)totalParcelas - 1, (int)totalParcelas);
            return parcelaRepository.findByEmprestimoAndNumeroParcelaIn(emprestimo, numerosParaBuscar);
        }

        int numeroParcelaFoco = numeroParcelaFocoOpt.get();
        List<Integer> numerosParaBuscar = new ArrayList<>();

        if (numeroParcelaFoco == 1)
        {
            numerosParaBuscar.addAll(List.of(1, 2, 3));
        }
        else
        {
            numerosParaBuscar.addAll(List.of(numeroParcelaFoco - 1, numeroParcelaFoco, numeroParcelaFoco + 1));
        }

        return parcelaRepository.findByEmprestimoAndNumeroParcelaIn(emprestimo, numerosParaBuscar);
    }
}
