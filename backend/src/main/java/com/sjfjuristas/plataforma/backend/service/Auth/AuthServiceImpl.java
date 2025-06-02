package com.sjfjuristas.plataforma.backend.service.Auth;

import com.sjfjuristas.plataforma.backend.domain.Usuario;
import com.sjfjuristas.plataforma.backend.dto.Usuario.AuthResponseDTO;
import com.sjfjuristas.plataforma.backend.dto.Usuario.ClienteCreateRequestDTO;
import com.sjfjuristas.plataforma.backend.dto.Usuario.LoginRequestDTO;
import com.sjfjuristas.plataforma.backend.repository.UsuarioRepository;
import com.sjfjuristas.plataforma.backend.service.Auth.AuthService;
import com.sjfjuristas.plataforma.backend.service.Jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponseDTO register(ClienteCreateRequestDTO request) {
        
        if (!request.getSenha().equals(request.getConfirmarSenha())) {
            throw new IllegalArgumentException("As senhas não coincidem.");
        }
        
        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("O e-mail informado já está em uso.");
        }
        
        Usuario novoUsuario = new Usuario();
        ZoneId fusoHorarioPadrao = ZoneId.systemDefault();
        novoUsuario.setNomeCompleto(request.getNomeCompleto());
        novoUsuario.setCpf(request.getCpf());
        novoUsuario.setEmail(request.getEmail());

        novoUsuario.setHashSenha(passwordEncoder.encode(request.getSenha()));
        novoUsuario.setTelefoneWhatsapp(request.getTelefoneWhatsapp());
        novoUsuario.setDataNascimento(LocalDate.parse(request.getDataNascimento(), DateTimeFormatter.ISO_LOCAL_DATE));
        novoUsuario.setAceitouTermosApp(false);
        novoUsuario.setEmailVerificado(false);
        novoUsuario.setDataCadastro(LocalDate.now().atStartOfDay(fusoHorarioPadrao).toOffsetDateTime());
        novoUsuario.setAtivo(true);
        
        //TODO: Definir um perfil padrão para o cliente
        // novoUsuario.setPerfilIdPerfisusuario(...);
        // definir como cliente após criar os perfis no banco de dados

        usuarioRepository.save(novoUsuario);


        // String jwtToken = jwtService.generateToken(novoUsuario);
        
        return AuthResponseDTO.builder()
                // .token(jwtToken)
                .usuarioId(novoUsuario.getId())
                .nomeUsuario(novoUsuario.getNomeCompleto())
                .build();
    }

    @Override
    public AuthResponseDTO login(LoginRequestDTO request) {
        // AuthenticationManager usará o UserDetailsService e o PasswordEncoder para validar
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha())
        );

        // Se a autenticação for bem-sucedida, busca o usuário para gerar o token
        var usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));
        
        ZoneId fusoHorarioPadrao = ZoneId.systemDefault();

        usuario.setUltimoLogin(LocalDate.now().atStartOfDay(fusoHorarioPadrao).toOffsetDateTime());

        String jwtToken = jwtService.generateToken(usuario);

        return AuthResponseDTO.builder()
                .token(jwtToken)
                .usuarioId(usuario.getId())
                .nomeUsuario(usuario.getNomeCompleto())
                .build();
    }
}