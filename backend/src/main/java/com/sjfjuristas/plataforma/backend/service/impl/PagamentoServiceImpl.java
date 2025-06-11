package com.sjfjuristas.plataforma.backend.service.impl;

import com.sjfjuristas.plataforma.backend.domain.*;
import com.sjfjuristas.plataforma.backend.dto.PagamentosParcela.PagamentoParcelaClienteRequestDTO;
import com.sjfjuristas.plataforma.backend.dto.PagamentosParcela.PagamentoParcelaResponseDTO;
import com.sjfjuristas.plataforma.backend.repository.*;
import com.sjfjuristas.plataforma.backend.service.PagamentoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class PagamentoServiceImpl implements PagamentoService {
    
    private static final Logger logger = LoggerFactory.getLogger(PagamentoServiceImpl.class);

    private final PagamentoParcelaRepository pagamentoRepository;
    private final ParcelaEmprestimoRepository parcelaRepository;
    // private final EmprestimoRepository emprestimoRepository;
    private final UsuarioRepository usuarioRepository;
    private final StatusPagamentoParcelaRepository statusPagamentoParcelaRepository;
    // private final ComprovantePagamentoService comprovanteService; // Se o upload for junto


    @Override
    @Transactional
    public PagamentoParcelaResponseDTO registrarPagamentoCliente(UUID clienteId, PagamentoParcelaClienteRequestDTO request) {
        logger.info("Registrando pagamento do cliente {} para parcela {}", clienteId, request.getParcelaId());
        
        Usuario cliente = usuarioRepository.findById(clienteId)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado: " + clienteId));
        ParcelaEmprestimo parcela = parcelaRepository.findById(request.getParcelaId())
                .orElseThrow(() -> new EntityNotFoundException("Parcela não encontrada: " + request.getParcelaId()));

        if (!parcela.getEmprestimoIdEmprestimos().getUsuarioIdUsuarios().getId().equals(clienteId)) {
            throw new SecurityException("Parcela não pertence ao cliente informado.");
        }
        
        if ("Pago".equalsIgnoreCase(parcela.getStatusPagamentoParcelaIdStatuspagamentoparcela().getNomeStatus())) {
             throw new IllegalStateException("Esta parcela já foi paga.");
        }

        PagamentoParcela pagamento = new PagamentoParcela();
        pagamento.setParcelaIdParcelasemprestimo(parcela);
        pagamento.setEmprestimoIdEmprestimos(parcela.getEmprestimoIdEmprestimos());
        pagamento.setUsuarioIdUsuarios(cliente);
        pagamento.setValorPago(request.getValorPago() != null ? request.getValorPago() : parcela.getValorTotalParcela()); // Usar valor da parcela se não informado
        pagamento.setDataPagamentoEfetivo(request.getDataPagamento() != null ? request.getDataPagamento() : OffsetDateTime.now());
        pagamento.setMeioPagamento("");
        pagamento.setIdTransacaoPagamentoPsp(request.getIdTransacaoPsp());

        PagamentoParcela savedPagamento = pagamentoRepository.save(pagamento);

        // Atualizar status da parcela e saldo devedor do empréstimo
        atualizarStatusPosPagamento(parcela, savedPagamento.getValorPago());
        
        logger.info("Pagamento {} registrado com sucesso para parcela {}", savedPagamento.getId(), parcela.getId());
        return mapToResponseDTO(savedPagamento);
    }

    private void atualizarStatusPosPagamento(ParcelaEmprestimo parcela, java.math.BigDecimal valorPago) {
        // Marcar parcela como paga
        StatusPagamentoParcela statusPago = statusPagamentoParcelaRepository.findByNomeStatus("Pago") // Ajuste nome
                .orElseThrow(() -> new EntityNotFoundException("Status 'Pago' não encontrado."));
        parcela.setStatusPagamentoParcelaIdStatuspagamentoparcela(statusPago);
        parcelaRepository.save(parcela);

        // Atualizar saldo devedor do empréstimo

        
        logger.warn("Lógica de atualização de saldo devedor e status do empréstimo precisa ser detalhada e implementada.");
    }
    
    // Implementar outros métodos da interface e o mapToResponseDTO
    private PagamentoParcelaResponseDTO mapToResponseDTO(PagamentoParcela pagamento) {
        // Implementar mapeamento
        return PagamentoParcelaResponseDTO.builder().id(pagamento.getId())
            //...
            .build();
    }

    @Override
    public PagamentoParcelaResponseDTO registrarPagamentoAdmin(UUID adminId, Object request) {
        // Implementar lógica para admin registrar pagamento
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<PagamentoParcelaResponseDTO> getHistoricoPagamentosEmprestimo(UUID emprestimoId) {
        // Implementar busca e mapeamento
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<PagamentoParcelaResponseDTO> getHistoricoPagamentosCliente(UUID clienteId) {
        // Implementar busca e mapeamento
        throw new UnsupportedOperationException("Not supported yet.");
    }
}