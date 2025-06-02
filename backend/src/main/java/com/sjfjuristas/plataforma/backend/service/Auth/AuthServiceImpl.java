package com.sjfjuristas.plataforma.backend.service.Auth;

import com.sjfjuristas.plataforma.backend.domain.Usuario;
import com.sjfjuristas.plataforma.backend.dto.Usuario.AuthResponseDTO;
import com.sjfjuristas.plataforma.backend.dto.Usuario.ClienteCreateRequestDTO;
import com.sjfjuristas.plataforma.backend.dto.Usuario.LoginRequestDTO;
import com.sjfjuristas.plataforma.backend.repository.UsuarioRepository;
import com.sjfjuristas.plataforma.backend.service.Auth.AuthService;
//import com.sjfjuristas.plataforma.backend.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    // private final JwtService jwtService; // Criaremos a seguir
    // private final AuthenticationManager authenticationManager; // Configuraremos a seguir

    @Override
    public AuthResponseDTO register(ClienteCreateRequestDTO request) {
        // 1. Validar se as senhas coincidem
        if (!request.getSenha().equals(request.getConfirmarSenha())) {
            throw new IllegalArgumentException("As senhas não coincidem.");
        }

        // 2. Verificar se o e-mail ou CPF já existem (opcional, mas recomendado)
        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("O e-mail informado já está em uso.");
        }

        // 3. Criar a entidade Usuario
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNomeCompleto(request.getNomeCompleto());
        novoUsuario.setCpf(request.getCpf());
        novoUsuario.setEmail(request.getEmail());
        // 4. Criptografar a senha antes de salvar
        novoUsuario.setHashSenha(passwordEncoder.encode(request.getSenha()));
        novoUsuario.setTelefoneWhatsapp(request.getTelefoneWhatsapp());
        novoUsuario.setDataNascimento(LocalDate.parse(request.getDataNascimento(), DateTimeFormatter.ISO_LOCAL_DATE));
        novoUsuario.setAceitouTermosApp(request.isAceitouTermos());
        
        // Definir um perfil padrão para o cliente
        // novoUsuario.setPerfilIdPerfisusuario(...); // Você precisará buscar o perfil "Solicitante" no banco

        usuarioRepository.save(novoUsuario);

        // 5. Gerar um token JWT para o novo usuário
        // String jwtToken = jwtService.generateToken(novoUsuario); // Usaremos UserDetails mais tarde
        
        return AuthResponseDTO.builder()
                // .token(jwtToken)
                .usuarioId(novoUsuario.getId())
                .nomeUsuario(novoUsuario.getNomeCompleto())
                .build();
    }

    @Override
    public AuthResponseDTO login(LoginRequestDTO request) {
        // A lógica de login será implementada após configurarmos o AuthenticationManager
        return null; 
    }
}