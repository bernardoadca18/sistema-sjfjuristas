package com.sjfjuristas.plataforma.backend.service.impl;

import com.sjfjuristas.plataforma.backend.domain.Emprestimo;
import com.sjfjuristas.plataforma.backend.domain.Usuario;
import com.sjfjuristas.plataforma.backend.dto.Emprestimos.EmprestimoClienteResponseDTO;
import com.sjfjuristas.plataforma.backend.dto.Emprestimos.EmprestimoAdminResponseDTO;
import com.sjfjuristas.plataforma.backend.dto.ParcelaEmprestimo.ParcelaEmprestimoResponseDTO;
import com.sjfjuristas.plataforma.backend.repository.EmprestimoRepository;
import com.sjfjuristas.plataforma.backend.repository.UsuarioRepository;
import com.sjfjuristas.plataforma.backend.service.EmprestimoService;
import com.sjfjuristas.plataforma.backend.service.ParcelaEmprestimoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmprestimoServiceImpl implements EmprestimoService {

    private final EmprestimoRepository emprestimoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ParcelaEmprestimoService parcelaEmprestimoService;

    @Override
    @Transactional(readOnly = true)
    public EmprestimoClienteResponseDTO getEmprestimoAtualCliente(UUID clienteId) {
        Usuario cliente = usuarioRepository.findById(clienteId)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com ID: " + clienteId));
        
        // Lógica para encontrar o empréstimo "atual" (ex: último ativo)
        Emprestimo emprestimo = emprestimoRepository.findByUsuarioIdUsuarios(cliente)
                .stream()
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Nenhum empréstimo atual encontrado para o cliente ID: " + clienteId));
        
        return mapToClienteResponseDTO(emprestimo);
    }

    @Override
    @Transactional(readOnly = true)
    public EmprestimoAdminResponseDTO getDetalhesEmprestimoAdmin(UUID emprestimoId) {
        Emprestimo emprestimo = emprestimoRepository.findById(emprestimoId)
                .orElseThrow(() -> new EntityNotFoundException("Empréstimo não encontrado com ID: " + emprestimoId));
        return mapToAdminResponseDTO(emprestimo);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<EmprestimoClienteResponseDTO> getHistoricoEmprestimosCliente(UUID clienteId) {
        Usuario cliente = usuarioRepository.findById(clienteId)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com ID: " + clienteId));
        return emprestimoRepository.findByUsuarioIdUsuarios(cliente).stream()
                .map(this::mapToClienteResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParcelaEmprestimoResponseDTO> getParcelasEmprestimo(UUID emprestimoId) {
        Emprestimo emprestimo = emprestimoRepository.findById(emprestimoId)
                .orElseThrow(() -> new EntityNotFoundException("Empréstimo não encontrado com ID: " + emprestimoId));
        // Delega para um ParcelaEmprestimoService ou busca diretamente no ParcelaEmprestimoRepository
        return parcelaEmprestimoService.getParcelasPorEmprestimo(emprestimo.getId());
    }

    // Métodos helper para mapeamento
    private EmprestimoClienteResponseDTO mapToClienteResponseDTO(Emprestimo emprestimo) {
        // Implemente o mapeamento completo aqui
        return EmprestimoClienteResponseDTO.builder()
                .id(emprestimo.getId())
                .valorContratado(emprestimo.getValorContratado())
                .saldoDevedorAtual(emprestimo.getSaldoDevedorAtual())
                .statusEmprestimoNome(emprestimo.getStatusEmprestimoIdStatusemprestimo() != null ? emprestimo.getStatusEmprestimoIdStatusemprestimo().getNomeStatus() : null)
                .build();
    }

    private EmprestimoAdminResponseDTO mapToAdminResponseDTO(Emprestimo emprestimo) {
        return EmprestimoAdminResponseDTO.builder()
                .id(emprestimo.getId())
                .build();
    }
}
