package com.sjfjuristas.plataforma.backend.service;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sjfjuristas.plataforma.backend.domain.Usuario;
import com.sjfjuristas.plataforma.backend.dto.Usuario.PasswordResetRequestDTO;
import com.sjfjuristas.plataforma.backend.dto.Usuario.PasswordSetNewWithTokenDTO;
import com.sjfjuristas.plataforma.backend.dto.Usuario.PasswordUpdateRequestDTO;
import com.sjfjuristas.plataforma.backend.exceptions.InvalidPasswordException;
import com.sjfjuristas.plataforma.backend.exceptions.InvalidTokenException;
import com.sjfjuristas.plataforma.backend.repository.UsuarioRepository;

@Service
public class PasswordResetService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Value("${sjfjuristas.link-frontend}")
    private String linkFrontend;

    @Value("${sjfjuristas.path-redefinicao-senha}")
    private String pathRedefinicaoSenha;


    private static final int TOKEN_VALIDITY_HOURS = 2;

    /**
     * Fluxo "Esqueci minha senha".
     * Gera um token de recuperação e o envia por e-mail para o usuário.
     */
    @Transactional
    public void solicitarRedefinicao(PasswordResetRequestDTO requestDTO)
    {
        Usuario usuario = usuarioRepository.findByEmail(requestDTO.getEmail()).orElseThrow(() -> new UsernameNotFoundException("Usuário не encontrado com o e-mail: " + requestDTO.getEmail()));

        String token = UUID.randomUUID().toString();
        usuario.setTokenRecuperacaoSenha(token);
        usuario.setValidadeTokenRecuperacao(OffsetDateTime.now().plusHours(TOKEN_VALIDITY_HOURS));

        usuarioRepository.save(usuario);

        String link = linkFrontend + pathRedefinicaoSenha + "?token=" + token;
        String assunto = "Redefinição de Senha - SJF Juristas";

        Map<String, Object> variaveis = new HashMap<>();
        variaveis.put("assunto", assunto);
        variaveis.put("nomeUsuario", usuario.getNomeCompleto());
        variaveis.put("linkRedefinicao", link);
        variaveis.put("validadeToken", TOKEN_VALIDITY_HOURS);

        emailService.enviarEmailHtml( usuario.getEmail(),  assunto,  "email-redefinicao-senha.html", variaveis );
    }

    /**
     * Fluxo "Esqueci minha senha".
     * Define uma nova senha usando o token de recuperação.
     */
    @Transactional
    public void redefinirComToken(PasswordSetNewWithTokenDTO dto) {
        if (!dto.getNovaSenha().equals(dto.getConfirmarNovaSenha())) {
            throw new InvalidPasswordException("As senhas não conferem.");
        }

        Usuario usuario = usuarioRepository.findByTokenRecuperacaoSenha(dto.getToken())
                .orElseThrow(() -> new InvalidTokenException("Token inválido ou expirado."));

        if (usuario.getValidadeTokenRecuperacao().isBefore(OffsetDateTime.now())) {
            // Limpa o token para segurança
            usuario.setTokenRecuperacaoSenha(null);
            usuario.setValidadeTokenRecuperacao(null);
            usuarioRepository.save(usuario);
            throw new InvalidTokenException("Token expirado. Por favor, solicite uma nova redefinição de senha.");
        }

        usuario.setHashSenha(passwordEncoder.encode(dto.getNovaSenha()));
        // Limpa o token após o uso para que não possa ser reutilizado
        usuario.setTokenRecuperacaoSenha(null);
        usuario.setValidadeTokenRecuperacao(null);

        usuarioRepository.save(usuario);
    }

    /**
     * Fluxo "Usuário Logado".
     * Atualiza a senha do usuário que está autenticado no sistema.
     */
    @Transactional
    public void atualizarSenhaUsuarioLogado(String userEmail, PasswordUpdateRequestDTO dto) {
        if (!dto.getNovaSenha().equals(dto.getConfirmarNovaSenha())) {
            throw new InvalidPasswordException("As novas senhas não conferem.");
        }

        Usuario usuario = usuarioRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));

        // Verifica se a senha atual fornecida corresponde à senha armazenada
        if (!passwordEncoder.matches(dto.getSenhaAtual(), usuario.getHashSenha())) {
            throw new InvalidPasswordException("A senha atual está incorreta.");
        }

        usuario.setHashSenha(passwordEncoder.encode(dto.getNovaSenha()));
        usuarioRepository.save(usuario);
    }
}