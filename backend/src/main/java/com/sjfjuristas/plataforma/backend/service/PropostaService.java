package com.sjfjuristas.plataforma.backend.service;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sjfjuristas.plataforma.backend.domain.Ocupacao;
import com.sjfjuristas.plataforma.backend.domain.PerfilUsuario;
import com.sjfjuristas.plataforma.backend.domain.PropostaEmprestimo;
import com.sjfjuristas.plataforma.backend.domain.PropostaHistorico;
import com.sjfjuristas.plataforma.backend.domain.StatusProposta;
import com.sjfjuristas.plataforma.backend.domain.Usuario;
import com.sjfjuristas.plataforma.backend.domain.enums.AtorAlteracao;
import com.sjfjuristas.plataforma.backend.dto.PropostasEmprestimo.ContrapropostaAdminRequestDTO;
import com.sjfjuristas.plataforma.backend.dto.PropostasEmprestimo.PropostaHistoricoResponseDTO;
import com.sjfjuristas.plataforma.backend.dto.PropostasEmprestimo.PropostaRequestDTO;
import com.sjfjuristas.plataforma.backend.dto.PropostasEmprestimo.PropostaResponseDTO;
import com.sjfjuristas.plataforma.backend.dto.PropostasEmprestimo.RespostaClienteDTO;
import com.sjfjuristas.plataforma.backend.repository.OcupacaoRepository;
import com.sjfjuristas.plataforma.backend.repository.PerfilUsuarioRepository;
import com.sjfjuristas.plataforma.backend.repository.PropostaEmprestimoRepository;
import com.sjfjuristas.plataforma.backend.repository.PropostaHistoricoRepository;
import com.sjfjuristas.plataforma.backend.repository.StatusPropostaRepository;
import com.sjfjuristas.plataforma.backend.repository.UsuarioRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class PropostaService
{
    @Autowired
    private PropostaEmprestimoRepository propostaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private StatusPropostaRepository statusPropostaRepository;

    @Autowired
    private PerfilUsuarioRepository perfilUsuarioRepository;

    @Autowired
    private OcupacaoRepository ocupacaoRepository;

    @Autowired
    private PropostaHistoricoRepository propostaHistoricoRepository;

    @Transactional
    public PropostaResponseDTO criarProposta(PropostaRequestDTO dto, HttpServletRequest request) {

        Usuario usuario = usuarioRepository.findByCpf(dto.getCpf()).orElseGet(() -> {
            Usuario novoUsuario = new Usuario();

            novoUsuario.setNomeCompleto(dto.getNomeCompleto());
            novoUsuario.setCpf(dto.getCpf());
            novoUsuario.setEmail(dto.getEmail());
            novoUsuario.setTelefoneWhatsapp(dto.getWhatsapp());
            novoUsuario.setDataNascimento(dto.getDataNascimento());

            
            PerfilUsuario perfilUsuarioCliente = perfilUsuarioRepository.findByNomePerfil("Cliente").orElseThrow(() -> new RuntimeException("Perfil 'Cliente' não encontrado."));
            novoUsuario.setPerfilIdPerfisusuario(perfilUsuarioCliente);
            return usuarioRepository.save(novoUsuario);
        });

        if (usuario.getDataNascimento() == null)
        {
            usuario.setDataNascimento(dto.getDataNascimento());
            usuarioRepository.save(usuario);
        }

        StatusProposta statusInicial = statusPropostaRepository.findByNomeStatus("Pendente de Análise").orElseThrow(() -> new RuntimeException("Status de proposta 'Pendente de Análise' não encontrado."));
        Ocupacao ocupacao = ocupacaoRepository.findById(dto.getOcupacaoId()).orElseThrow(() -> new IllegalArgumentException("Ocupação com ID " + dto.getOcupacaoId() + " não encontrada."));
        PropostaEmprestimo novaProposta = new PropostaEmprestimo();
        
        novaProposta.setValorSolicitado(dto.getValorSolicitado());
        novaProposta.setNomeCompletoSolicitante(dto.getNomeCompleto());
        novaProposta.setCpfSolicitante(dto.getCpf());
        novaProposta.setEmailSolicitante(dto.getEmail());
        novaProposta.setTelefoneWhatsappSolicitante(dto.getWhatsapp());
        novaProposta.setTermosAceitosLp(dto.getTermosAceitos());
        novaProposta.setDataNascimentoSolicitante(dto.getDataNascimento());
        novaProposta.setNumParcelasPreferido(dto.getNumParcelasPreferido());
        novaProposta.setRemuneracaoMensalSolicitante(dto.getRemuneracaoMensal());
        novaProposta.setOcupacao(ocupacao);
        novaProposta.setPropositoEmprestimo(dto.getPropositoEmprestimo());
        novaProposta.setEstadoCivil(dto.getEstadoCivil());
        novaProposta.setPossuiImovelVeiculo(dto.getPossuiImovelVeiculo());

        if ("Outros".equalsIgnoreCase(ocupacao.getNomeOcupacao()))
        {
            novaProposta.setOutraOcupacaoSolicitante(dto.getOutraOcupacao());
        }

        // Dados preenchidos pelo backend
        novaProposta.setDataSolicitacao(OffsetDateTime.now());
        
        if (dto.getTermosAceitos())
        {
            novaProposta.setDataAceiteTermosLp(OffsetDateTime.now());
        }
        
        novaProposta.setIpSolicitacao(request.getRemoteAddr());
        novaProposta.setUserAgentSolicitacao(request.getHeader("User-Agent"));
        novaProposta.setOrigemCaptacao("LandingPage");

        // Associações
        novaProposta.setStatusPropostaIdStatusproposta(statusInicial);
        novaProposta.setUsuarioIdUsuarios(usuario);

        PropostaEmprestimo propostaSalva = propostaRepository.save(novaProposta);

        salvarHistorico(null, propostaSalva, AtorAlteracao.CLIENTE, "Proposta inicial criada pelo cliente via Landing Page.");

        return new PropostaResponseDTO(propostaSalva);
    }

    @Transactional
    public PropostaEmprestimo enviarContrapropostaAdmin(UUID propostaId, ContrapropostaAdminRequestDTO dto)
    {
        PropostaEmprestimo propostaAtual = propostaRepository.findById(propostaId).orElseThrow(() -> new EntityNotFoundException("Proposta não encontrada com o ID: " + propostaId));

        PropostaEmprestimo propostaAntes = cloneProposta(propostaAtual);

        propostaAtual.setValorOfertado(dto.getValorOfertado());
        propostaAtual.setTaxaJurosOfertada(dto.getTaxaJurosOfertada());
        propostaAtual.setNumParcelasOfertado(dto.getNumParcelasOfertado());
        propostaAtual.setDataDepositoPrevista(dto.getDataDepositoPrevista());
        propostaAtual.setDataInicioPagamentoPrevista(dto.getDataInicioPagamentoPrevista());
        propostaAtual.setOrigemUltimaOferta(AtorAlteracao.ADMIN.name());

        StatusProposta novoStatus = statusPropostaRepository.findByNomeStatus("Contraproposta Enviada").orElseThrow(() -> new EntityNotFoundException("Status 'Contraproposta Enviada' não encontrado."));
        propostaAtual.setStatusPropostaIdStatusproposta(novoStatus);

        PropostaEmprestimo propostaSalva = propostaRepository.save(propostaAtual);

        salvarHistorico(propostaAntes, propostaSalva, AtorAlteracao.ADMIN, "Contraproposta enviada pelo administrador.");

        return propostaSalva;
    }

    @Transactional
    public PropostaEmprestimo responderContrapropostaCliente(UUID propostaId, RespostaClienteDTO resposta, UUID usuarioId)
    {
        PropostaEmprestimo propostaAtual = propostaRepository.findById(propostaId).orElseThrow(() -> new EntityNotFoundException("Proposta não encontrada com o ID: " + propostaId));

        if (!propostaAtual.getUsuarioIdUsuarios().getId().equals(usuarioId))
        {
            throw new AccessDeniedException("Acesso negado. Esta proposta não pertence ao usuário autenticado.");
        }

        PropostaEmprestimo propostaAntes = cloneProposta(propostaAtual);
        String novoStatusNome;
        String observacao;

        if (resposta.getAceite())
        {
            novoStatusNome = "Contraproposta Aceita";
            observacao = "Cliente aceitou a contraproposta do administrador";
        }
        else
        {
            novoStatusNome = "Contraproposta Recusada";
            propostaAtual.setMotivoRecusaCliente(resposta.getMotivoRecusa());
            observacao = "Cliente recusou a contraproposta. Motivo: " + resposta.getMotivoRecusa();
        }

        StatusProposta novoStatus = statusPropostaRepository.findByNomeStatus(novoStatusNome).orElseThrow(() -> new EntityNotFoundException("Status '" + novoStatusNome + "' não encontrado."));
        propostaAtual.setStatusPropostaIdStatusproposta(novoStatus);

        PropostaEmprestimo propostaSalva = propostaRepository.save(propostaAtual);
        salvarHistorico(propostaAntes, propostaSalva, AtorAlteracao.CLIENTE, observacao);

        return propostaSalva;
    }

    @Transactional
    public void negarPropostaAdmin(UUID propostaId, String motivo)
    {
        PropostaEmprestimo propostaAtual = propostaRepository.findById(propostaId).orElseThrow(() -> new EntityNotFoundException("Proposta não encontrada com o ID: " + propostaId));
        PropostaEmprestimo propostaAntes = cloneProposta(propostaAtual);

        propostaAtual.setMotivoNegacao(motivo);
        StatusProposta novoStatus = statusPropostaRepository.findByNomeStatus("Negada").orElseThrow(() -> new EntityNotFoundException("Status 'Negada' não encontrado."));
        propostaAtual.setStatusPropostaIdStatusproposta(novoStatus);

        PropostaEmprestimo propostaSalva = propostaRepository.save(propostaAtual);
        salvarHistorico(propostaAntes, propostaSalva, AtorAlteracao.ADMIN, "Proposta negada. Motivo: " + motivo);

        // TODO: Implementar o envio de notificação para o cliente sobre a recusa.
    }

    @Transactional
    public void atualizarStatus(UUID propostaId, String novoStatusNome)
    {
        PropostaEmprestimo proposta = propostaRepository.findById(propostaId).orElseThrow(() -> new EntityNotFoundException("Proposta não encontrada"));
        StatusProposta novoStatus = statusPropostaRepository.findByNomeStatus(novoStatusNome).orElseThrow(() -> new IllegalStateException("Status '" + novoStatusNome + "' inválido."));     
        proposta.setStatusPropostaIdStatusproposta(novoStatus);
        
        propostaRepository.save(proposta);
    }

    @Transactional
    public PropostaEmprestimo processarRespostaCliente(UUID propostaId, RespostaClienteDTO resposta, UUID usuarioId)
    {
        PropostaEmprestimo propostaAtual = propostaRepository.findById(propostaId).orElseThrow(() -> new EntityNotFoundException("Proposta não encontrada"));
        
        if (!propostaAtual.getUsuarioIdUsuarios().getId().equals(usuarioId))
        {
            throw new AccessDeniedException("Acesso negado. Esta proposta não pertence ao usuário autenticado.");
        }

        PropostaEmprestimo propostaAntes = cloneProposta(propostaAtual);
        String novoStatusNome;
        String observacao;

        switch (resposta.getAcaoCliente())
        {
            case ACEITAR -> {
                novoStatusNome = "Contraproposta Aceita";
                observacao = "Cliente aceitou a contraproposta.";
            }
            case RECUSAR -> {
                novoStatusNome = "Contraproposta Recusada";
                propostaAtual.setMotivoRecusaCliente(resposta.getMotivoRecusa());
                observacao = "Cliente recusou a contraproposta.";
            }
            case CONTRAPROPOR -> {
                novoStatusNome = "Pendente de Análise";
                propostaAtual.setValorOfertado(resposta.getValorContrapropostaOpt().orElseThrow(() -> new IllegalArgumentException("Valor da contraproposta é obrigatório.")));
                propostaAtual.setNumParcelasOfertado(resposta.getNumParcelasContrapropostaOpt().orElseThrow(() -> new IllegalArgumentException("Número de parcelas da contraproposta é obrigatório.")));
                propostaAtual.setOrigemUltimaOferta("CLIENTE");
                observacao = "Cliente enviou uma nova contraproposta.";
            }
            default -> throw new IllegalArgumentException("Ação inválida.");
        }

        StatusProposta novoStatus = statusPropostaRepository.findByNomeStatus(novoStatusNome).orElseThrow(() -> new EntityNotFoundException("Status '" + novoStatusNome + "' não encontrado."));
        propostaAtual.setStatusPropostaIdStatusproposta(novoStatus);

        PropostaEmprestimo propostaSalva = propostaRepository.save(propostaAtual);
        salvarHistorico(propostaAntes, propostaSalva, AtorAlteracao.CLIENTE, observacao);
        
        // TODO: Enviar notificação para o Admin (em caso de aceite ou nova contraproposta)
        return propostaRepository.save(propostaSalva);
    }

    @Transactional(readOnly = true)
    public Page<PropostaHistoricoResponseDTO> getHistoricoProposta(UUID propostaId, Pageable pageable)
    {
        if (!propostaRepository.existsById(propostaId))
        {
            throw new EntityNotFoundException("Proposta não encontrada");
        }
        
        Page<PropostaHistorico> paginaHistorico = propostaHistoricoRepository.findByPropostaIdOrderByDataAlteracaoDesc(propostaId, pageable);

        return paginaHistorico.map(h -> PropostaHistoricoResponseDTO.builder()
        .id(h.getId())
        .dataAlteracao(h.getDataAlteracao())
        .atorAlteracao(h.getAtorAlteracao())
        .statusAnterior(h.getStatusAnterior())
        .statusNovo(h.getStatusNovo())
        .valorAnterior(h.getValorAnterior())
        .valorNovo(h.getValorNovo())
        .numParcelasAnterior(h.getNumParcelasAnterior())
        .numParcelasNovo(h.getNumParcelasNovo())
        .taxaJurosAnterior(h.getTaxaJurosAnterior())
        .taxaJurosNova(h.getTaxaJurosNova())
        .motivoRecusa(h.getMotivoRecusa())
        .observacoes(h.getObservacoes())
        .build());
    }

    @Transactional(readOnly = true)
    public Page<PropostaHistoricoResponseDTO> getHistoricoPropostaCliente(UUID propostaId, UUID clienteId, Pageable pageable)
    {
        PropostaEmprestimo proposta = propostaRepository.findById(propostaId).orElseThrow(() -> new EntityNotFoundException("Proposta não encontrada"));

        if (!proposta.getUsuarioIdUsuarios().getId().equals(clienteId))
        {
            throw new org.springframework.security.access.AccessDeniedException("Acesso negado.");
        }

        return getHistoricoProposta(propostaId, pageable);
    }

    private void salvarHistorico(PropostaEmprestimo antes, PropostaEmprestimo depois, AtorAlteracao ator, String observacoes)
    {
        PropostaHistorico registroHistorico = new PropostaHistorico();

        registroHistorico.setProposta(depois);
        registroHistorico.setAtorAlteracao(ator.name());
        registroHistorico.setObservacoes(observacoes);
        registroHistorico.setDataAlteracao(OffsetDateTime.now());

        if (antes == null)
        {
            registroHistorico.setStatusAnterior(null);
            registroHistorico.setStatusNovo((depois.getStatusPropostaIdStatusproposta().getNomeStatus()));
            registroHistorico.setValorAnterior(null);
            registroHistorico.setValorNovo(depois.getValorOfertado());
            registroHistorico.setNumParcelasAnterior(null);
            registroHistorico.setNumParcelasNovo(depois.getNumParcelasPreferido());
            registroHistorico.setTaxaJurosAnterior(null);
            registroHistorico.setTaxaJurosNova(null);
        }
        else
        {
            registroHistorico.setStatusAnterior((antes.getStatusPropostaIdStatusproposta().getNomeStatus()));
            registroHistorico.setStatusNovo((depois.getStatusPropostaIdStatusproposta().getNomeStatus()));
            registroHistorico.setValorAnterior(antes.getValorOfertado());
            registroHistorico.setValorNovo(depois.getValorOfertado());
            registroHistorico.setNumParcelasAnterior(antes.getNumParcelasPreferido());
            registroHistorico.setNumParcelasNovo(depois.getNumParcelasPreferido());
            registroHistorico.setTaxaJurosAnterior(antes.getTaxaJurosOfertada());
            registroHistorico.setTaxaJurosNova(depois.getTaxaJurosOfertada());
        }

        registroHistorico.setMotivoRecusa(depois.getMotivoRecusaCliente() != null ? depois.getMotivoRecusaCliente() : depois.getMotivoNegacao());

        propostaHistoricoRepository.save(registroHistorico);
    }

    private PropostaEmprestimo cloneProposta(PropostaEmprestimo original)
    {
        PropostaEmprestimo clone = new PropostaEmprestimo();
        BeanUtils.copyProperties(original, clone);
        
        if (original.getStatusPropostaIdStatusproposta() != null)
        {
            clone.setStatusPropostaIdStatusproposta(original.getStatusPropostaIdStatusproposta());
        }

        return clone;
    }
}
