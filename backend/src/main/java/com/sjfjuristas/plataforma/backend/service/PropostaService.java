package com.sjfjuristas.plataforma.backend.service;

import com.sjfjuristas.plataforma.backend.domain.PerfilUsuario;
import com.sjfjuristas.plataforma.backend.domain.PropostaEmprestimo;
import com.sjfjuristas.plataforma.backend.domain.StatusProposta;
import com.sjfjuristas.plataforma.backend.domain.Usuario;
import com.sjfjuristas.plataforma.backend.dto.PropostasEmprestimo.PropostaRequestDTO;
import com.sjfjuristas.plataforma.backend.dto.PropostasEmprestimo.PropostaResponseDTO;
import com.sjfjuristas.plataforma.backend.repository.PerfilUsuarioRepository;
import com.sjfjuristas.plataforma.backend.repository.PropostaEmprestimoRepository;
import com.sjfjuristas.plataforma.backend.repository.StatusPropostaRepository;
import com.sjfjuristas.plataforma.backend.repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
public class PropostaService {

    @Autowired
    private PropostaEmprestimoRepository propostaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private StatusPropostaRepository statusPropostaRepository;

    // Você também precisará do PerfilUsuarioRepository injetado aqui
    @Autowired
    private PerfilUsuarioRepository perfilUsuarioRepository;

    @Transactional
    public PropostaResponseDTO criarProposta(PropostaRequestDTO dto, HttpServletRequest request) {

        Usuario usuario = usuarioRepository.findByCpf(dto.getCpf()).orElseGet(() -> {
            Usuario novoUsuario = new Usuario();
            novoUsuario.setNomeCompleto(dto.getNomeCompleto());
            novoUsuario.setCpf(dto.getCpf());
            novoUsuario.setEmail(dto.getEmail());
            novoUsuario.setTelefoneWhatsapp(dto.getWhatsapp());
            // Definir um perfil padrão "Cliente" aqui
            PerfilUsuario perfilUsuarioCliente = perfilUsuarioRepository.findByNomePerfil("Cliente").get();
            novoUsuario.setPerfilIdPerfisusuario(perfilUsuarioCliente);
            return usuarioRepository.save(novoUsuario);
        });

        StatusProposta statusInicial = statusPropostaRepository.findByNomeStatus("Pendente de Análise")
                .orElseThrow(() -> new RuntimeException("Status de proposta 'Pendente de Análise' não encontrado."));

        PropostaEmprestimo novaProposta = new PropostaEmprestimo();
        novaProposta.setValorSolicitado(dto.getValorSolicitado());
        novaProposta.setNomeCompletoSolicitante(dto.getNomeCompleto());
        novaProposta.setCpfSolicitante(dto.getCpf());
        novaProposta.setEmailSolicitante(dto.getEmail());
        novaProposta.setTelefoneWhatsappSolicitante(dto.getWhatsapp());
        novaProposta.setTermosAceitosLp(dto.getTermosAceitos());

        // Dados preenchidos pelo backend
        novaProposta.setDataSolicitacao(OffsetDateTime.now());
        if (dto.getTermosAceitos()) {
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
}
