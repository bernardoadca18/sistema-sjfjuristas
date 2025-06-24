package com.sjfjuristas.plataforma.backend.service;

import com.sjfjuristas.plataforma.backend.domain.PerfilUsuario;
import com.sjfjuristas.plataforma.backend.domain.PropostaEmprestimo;
import com.sjfjuristas.plataforma.backend.domain.StatusProposta;
import com.sjfjuristas.plataforma.backend.domain.Usuario;
import com.sjfjuristas.plataforma.backend.dto.PropostasEmprestimo.ContrapropostaAdminRequestDTO;
import com.sjfjuristas.plataforma.backend.dto.PropostasEmprestimo.PropostaRequestDTO;
import com.sjfjuristas.plataforma.backend.dto.PropostasEmprestimo.PropostaResponseDTO;
import com.sjfjuristas.plataforma.backend.repository.PerfilUsuarioRepository;
import com.sjfjuristas.plataforma.backend.repository.PropostaEmprestimoRepository;
import com.sjfjuristas.plataforma.backend.repository.StatusPropostaRepository;
import com.sjfjuristas.plataforma.backend.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;
import java.util.UUID;
import com.sjfjuristas.plataforma.backend.domain.Ocupacao;
import com.sjfjuristas.plataforma.backend.repository.OcupacaoRepository;

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

        return new PropostaResponseDTO(
            propostaSalva.getId(),
            propostaSalva.getValorSolicitado(),
            propostaSalva.getNomeCompletoSolicitante(),
            propostaSalva.getEmailSolicitante(),
            propostaSalva.getDataSolicitacao(),
            propostaSalva.getStatusPropostaIdStatusproposta().getNomeStatus()
        );
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
    public PropostaEmprestimo salvarContrapropostaAdmin(UUID propostaId, ContrapropostaAdminRequestDTO dto)
    {
        PropostaEmprestimo proposta = propostaRepository.findById(propostaId).orElseThrow(() -> new EntityNotFoundException("Proposta não encontrada"));

        proposta.setValorOfertado(dto.getValorOfertado());
        proposta.setTaxaJurosOfertada(dto.getTaxaJurosOfertada());
        proposta.setNumParcelasOfertado(dto.getNumParcelasOfertado());
        proposta.setDataDepositoPrevista(dto.getDataDepositoPrevista());
        proposta.setDataInicioPagamentoPrevista(dto.getDataInicioPagamentoPrevista());
        proposta.setOrigemUltimaOferta("ADMIN");
        
        atualizarStatus(propostaId, "Contraproposta Enviada");
        
        // TODO: Enviar notificação para o cliente
        
        return propostaRepository.save(proposta);
    }
    
    @Transactional
    public void negarProposta(UUID propostaId, String motivo)
    {
        PropostaEmprestimo proposta = propostaRepository.findById(propostaId).orElseThrow(() -> new EntityNotFoundException("Proposta não encontrada"));
        
        proposta.setMotivoNegacao(motivo);
        atualizarStatus(propostaId, "Negada");

        
        propostaRepository.save(proposta);
    }
}
