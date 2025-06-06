package com.sjfjuristas.plataforma.backend.service.impl;

import com.sjfjuristas.plataforma.backend.domain.Administrador;
import com.sjfjuristas.plataforma.backend.domain.PropostaEmprestimo;
import com.sjfjuristas.plataforma.backend.domain.StatusProposta;
import com.sjfjuristas.plataforma.backend.domain.Usuario;
// Adicione import para Emprestimo se a criação for aqui
// import com.sjfjuristas.plataforma.backend.domain.Emprestimo;
import com.sjfjuristas.plataforma.backend.dto.PropostasEmprestimo.PropostaEmprestimoCreateLPRequestDTO;
import com.sjfjuristas.plataforma.backend.dto.PropostasEmprestimo.PropostaEmprestimoResponseDTO;
import com.sjfjuristas.plataforma.backend.dto.PropostasEmprestimo.PropostaEmprestimoAdminActionRequestDTO;
import com.sjfjuristas.plataforma.backend.repository.AdministradorRepository;
import com.sjfjuristas.plataforma.backend.repository.PropostaEmprestimoRepository;
import com.sjfjuristas.plataforma.backend.repository.StatusPropostaRepository;
import com.sjfjuristas.plataforma.backend.repository.UsuarioRepository;
// Adicione import para EmprestimoRepository se a criação for aqui
// import com.sjfjuristas.plataforma.backend.repository.EmprestimoRepository;
import com.sjfjuristas.plataforma.backend.service.PropostaEmprestimoService;
// Adicione import para DocumentoPropostaService se for delegar
// import com.sjfjuristas.plataforma.backend.service.DocumentoPropostaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Admin;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Set;
import java.time.OffsetDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PropostaEmprestimoServiceImpl implements PropostaEmprestimoService {

    private static final Logger logger = LoggerFactory.getLogger(PropostaEmprestimoServiceImpl.class);

    private final PropostaEmprestimoRepository propostaRepository;
    private final UsuarioRepository usuarioRepository; // Para associar proposta a usuário existente ou criar novo
    private final AdministradorRepository administradorRepository; // Para associar proposta a admin que analisou
    private final StatusPropostaRepository statusPropostaRepository;
    // private final EmprestimoRepository emprestimoRepository; // Se criar Empréstimo aqui
    // private final DocumentoPropostaService documentoPropostaService; // Se delegar upload

    @Override
    @Transactional
    public PropostaEmprestimoResponseDTO criarPropostaLandingPage(PropostaEmprestimoCreateLPRequestDTO request) {
        logger.info("Criando proposta da landing page para CPF: {}", request.getCpfSolicitante());

        PropostaEmprestimo proposta = new PropostaEmprestimo();
        proposta.setValorSolicitado(request.getValorSolicitado());
        proposta.setNomeCompletoSolicitante(request.getNomeCompletoSolicitante());
        proposta.setCpfSolicitante(request.getCpfSolicitante());
        proposta.setEmailSolicitante(request.getEmailSolicitante());
        proposta.setTelefoneWhatsappSolicitante(request.getTelefoneWhatsappSolicitante());
        proposta.setTermosAceitosLp(request.getTermosAceitosLp());
        proposta.setDataAceiteTermosLp(request.getTermosAceitosLp() ? OffsetDateTime.now() : null);
        // Definir status inicial (ex: "Pendente de Análise")
        StatusProposta statusInicial = statusPropostaRepository.findByNomeStatus("Pendente de Análise") // Ajuste o nome do status
                .orElseThrow(() -> new EntityNotFoundException("Status inicial de proposta não configurado."));
        proposta.setStatusPropostaIdStatusproposta(statusInicial);

        // Tenta vincular a um usuário existente pelo CPF ou email
        usuarioRepository.findByCpf(request.getCpfSolicitante()).ifPresent(proposta::setUsuarioIdUsuarios);
        usuarioRepository.findByEmail(request.getEmailSolicitante()).ifPresent(proposta::setUsuarioIdUsuarios);
        
        PropostaEmprestimo savedProposta = propostaRepository.save(proposta);
        logger.info("Proposta {} criada com sucesso.", savedProposta.getId());
        return mapToResponseDTO(savedProposta);
    }

    @Override
    @Transactional(readOnly = true)
    public PropostaEmprestimoResponseDTO getPropostaById(UUID propostaId) {
        PropostaEmprestimo proposta = propostaRepository.findById(propostaId)
                .orElseThrow(() -> new EntityNotFoundException("Proposta não encontrada com ID: " + propostaId));
        return mapToResponseDTO(proposta);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PropostaEmprestimoResponseDTO> getAllPropostas() {
        return propostaRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PropostaEmprestimoResponseDTO> getPropostasPorCliente(UUID clienteId) {
        Usuario cliente = usuarioRepository.findById(clienteId)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com ID: " + clienteId));
        return propostaRepository.findByUsuarioIdUsuarios(cliente).stream() // Ajuste o nome do método do repo
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public PropostaEmprestimoResponseDTO processarAcaoAdmin(UUID propostaId, PropostaEmprestimoAdminActionRequestDTO request, UUID adminId) {
        PropostaEmprestimo proposta = propostaRepository.findById(propostaId)
                .orElseThrow(() -> new EntityNotFoundException("Proposta não encontrada com ID: " + propostaId));
        
        Administrador admin = administradorRepository.findById(adminId)
                .orElseThrow(() -> new EntityNotFoundException("Administrador não encontrado com ID: " + adminId));
        
        // Validar se o usuário é admin (verificar perfil) - ADICIONAR ESSA LÓGICA

        StatusProposta novoStatus = statusPropostaRepository.findById(request.getStatusPropostaId()) // Supondo que o DTO envia o ID
                .orElseThrow(() -> new EntityNotFoundException("Status de proposta inválido: " + request.getStatusPropostaId()));
    
        proposta.setStatusPropostaIdStatusproposta(novoStatus);

        Set<Administrador> administradoresDaProposta = proposta.getAdministradores();
        
        if (administradoresDaProposta == null || administradoresDaProposta.isEmpty()) {
            administradoresDaProposta = new LinkedHashSet<>();
        }

        if (!administradoresDaProposta.contains(admin))
        {
            administradoresDaProposta.add(admin);
        }

        proposta.setAdministradores(administradoresDaProposta);


        proposta.setAdministradores(null); // Mapeie para o campo correto da entidade
        proposta.setDataAnalise(OffsetDateTime.now());

        if ("Negada".equalsIgnoreCase(novoStatus.getNomeStatus())) { // Ajuste nome do status
            if (request.getMotivoNegacao() == null || request.getMotivoNegacao().isBlank()) {
                throw new IllegalArgumentException("Motivo da negação é obrigatório.");
            }
            proposta.setMotivoNegacao(request.getMotivoNegacao());
        } else if ("Aprovada".equalsIgnoreCase(novoStatus.getNomeStatus())) { // Ajuste nome do status
            // criar o Empréstimo se a proposta for aprovada
            logger.warn("Lógica de criação de Empréstimo a partir da aprovação da proposta precisa ser implementada aqui.");
        }
        
        // Adicionar outras observações se houver no DTO
        // proposta.setObservacoesAnalise(request.getObservacoesAnalise());

        PropostaEmprestimo updatedProposta = propostaRepository.save(proposta);
        return mapToResponseDTO(updatedProposta);
    }

    // Método helper para mapear Entidade para DTO
    private PropostaEmprestimoResponseDTO mapToResponseDTO(PropostaEmprestimo proposta) {
        if (proposta == null) {
            throw new EntityNotFoundException("Proposta não encontrada.");
        }
        
        String nomeUltimoAdmin = null;
        if (proposta.getAdministradores() != null && !proposta.getAdministradores().isEmpty()) {
            for (Administrador admin : proposta.getAdministradores()) {
                nomeUltimoAdmin = admin.getNomeCompleto();
            }
        }

        return PropostaEmprestimoResponseDTO.builder()
                .id(proposta.getId())
                .valorSolicitado(proposta.getValorSolicitado())
                .nomeCompletoSolicitante(proposta.getNomeCompletoSolicitante())
                .cpfSolicitante(proposta.getCpfSolicitante())
                .emailSolicitante(proposta.getEmailSolicitante())
                .telefoneWhatsappSolicitante(proposta.getTelefoneWhatsappSolicitante())
                .dataSolicitacao(proposta.getDataSolicitacao())
                .statusPropostaNome(proposta.getStatusPropostaIdStatusproposta() != null ? proposta.getStatusPropostaIdStatusproposta().getNomeStatus() : null)
                .analisadoPorAdminNome(((proposta.getAdministradores()!= null) && (nomeUltimoAdmin != null) && (!nomeUltimoAdmin.equals(null))) ? nomeUltimoAdmin : null)
                .dataAnalise(proposta.getDataAnalise())
                .motivoNegacao(proposta.getMotivoNegacao())
                // .documentos(proposta.getDocumentosPropostas().stream().map(doc -> mapToDocumentoSummaryDTO(doc)).collect(Collectors.toList()))
                .build();
    }
}