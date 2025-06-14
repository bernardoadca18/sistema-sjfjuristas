package com.sjfjuristas.plataforma.backend.service;

import com.sjfjuristas.plataforma.backend.domain.PerfilUsuario;
import com.sjfjuristas.plataforma.backend.domain.PropostaEmprestimo;
import com.sjfjuristas.plataforma.backend.domain.StatusProposta;
import com.sjfjuristas.plataforma.backend.domain.Usuario;
import com.sjfjuristas.plataforma.backend.dto.PropostasEmprestimo.PropostaEmprestimoCreateLPRequestDTO;
import com.sjfjuristas.plataforma.backend.repository.PerfilUsuarioRepository;
import com.sjfjuristas.plataforma.backend.repository.PropostaEmprestimoRepository;
import com.sjfjuristas.plataforma.backend.repository.StatusPropostaRepository;
import com.sjfjuristas.plataforma.backend.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.time.OffsetDateTime;
import java.util.List;

@Service
public class PropostaService {

    @Autowired
    private PropostaEmprestimoRepository propostaRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private StatusPropostaRepository statusPropostaRepository; 

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PerfilUsuarioRepository perfilUsuarioRepository;


    @Transactional
    public void criarProposta(PropostaEmprestimoCreateLPRequestDTO dto, List<MultipartFile> files) {
        
        Usuario usuario = usuarioRepository.findByEmail(dto.getEmailSolicitante())
        .or(() -> usuarioRepository.findByCpf(dto.getCpfSolicitante()))
        .orElseGet(() -> {
            PerfilUsuario perfilCliente = perfilUsuarioRepository.findByNomePerfil("Cliente")
            .orElseThrow(() -> new IllegalStateException("Perfil 'Cliente' não encontrado no banco de dados."));

            Usuario novoUsuario = new Usuario();
            novoUsuario.setNomeCompleto(dto.getNomeCompletoSolicitante());
            novoUsuario.setEmail(dto.getEmailSolicitante());
            novoUsuario.setCpf(dto.getCpfSolicitante());
            novoUsuario.setTelefoneWhatsapp(dto.getTelefoneWhatsappSolicitante());
            novoUsuario.setPerfilIdPerfisusuario(perfilCliente);
            novoUsuario.setAtivo(false);
            novoUsuario.setEmailVerificado(false);

            return usuarioRepository.save(novoUsuario);
        });
        
        StatusProposta statusInicial = statusPropostaRepository.findByNomeStatus("Pendente de Análise").orElseThrow(() -> new IllegalStateException("Status 'Pendente de Análise' não encontrado no banco de dados."));

        PropostaEmprestimo proposta = new PropostaEmprestimo();
        proposta.setValorSolicitado(dto.getValorSolicitado());
        proposta.setNomeCompletoSolicitante(dto.getNomeCompletoSolicitante());
        proposta.setCpfSolicitante(dto.getCpfSolicitante());
        proposta.setEmailSolicitante(dto.getEmailSolicitante());
        proposta.setTelefoneWhatsappSolicitante(dto.getTelefoneWhatsappSolicitante());
        proposta.setTermosAceitosLp(dto.getTermosAceitosLp());
        proposta.setDataSolicitacao(OffsetDateTime.now());

        proposta.setStatusPropostaIdStatusproposta(statusInicial);

        proposta.setIpSolicitacao(""); // Placeholder for IP address
        proposta.setUserAgentSolicitacao("");

        if (dto.getTermosAceitosLp())
        {
            proposta.setDataAceiteTermosLp(OffsetDateTime.now());
        }

        proposta.setLinkAppEnviado(false);
        proposta.setOrigemCaptacao("LandingPage");

        proposta.setUsuarioIdUsuarios(usuario);

        PropostaEmprestimo savedProposta = propostaRepository.save(proposta);

        for (MultipartFile file : files) {
            fileStorageService.uploadFile(file, savedProposta.getId().toString());
        }
    }
}
