package com.sjfjuristas.plataforma.backend.service.Auth;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sjfjuristas.plataforma.backend.domain.Usuario;
import com.sjfjuristas.plataforma.backend.dto.Usuario.AuthResponseDTO;
import com.sjfjuristas.plataforma.backend.dto.Usuario.ClienteCreateRequestDTO;
import com.sjfjuristas.plataforma.backend.dto.Usuario.FinalizarCadastroDTO;
import com.sjfjuristas.plataforma.backend.dto.Usuario.LoginRequestDTO;
import com.sjfjuristas.plataforma.backend.dto.Usuario.PreCadastroCheckDTO;
import com.sjfjuristas.plataforma.backend.dto.Usuario.PreCadastroInfoDTO;
import com.sjfjuristas.plataforma.backend.exceptions.RegistrationConflictException;
import com.sjfjuristas.plataforma.backend.exceptions.UserNotFoundException;
import com.sjfjuristas.plataforma.backend.repository.ChavePixUsuarioRepository;
import com.sjfjuristas.plataforma.backend.repository.UsuarioRepository;
import com.sjfjuristas.plataforma.backend.service.Jwt.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ChavePixUsuarioRepository chavePixRepository;

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
    

        usuarioRepository.save(novoUsuario);
        
        return AuthResponseDTO.builder()
                .usuarioId(novoUsuario.getId())
                .nomeUsuario(novoUsuario.getNomeCompleto())
                .build();
    }

    @Override
    public AuthResponseDTO login(LoginRequestDTO request) 
    {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha())
        );

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

    @Override
    public PreCadastroInfoDTO verificarPreCadastro(PreCadastroCheckDTO request)
    {
        Optional<Usuario> usuarioOpt = buscarUsuarioPorPreCadastro(request);

        if (usuarioOpt.isEmpty())
        {
            throw new UserNotFoundException("Nenhum cadastro inicial encontrado para os dados informados.");
        }

        Usuario usuario = usuarioOpt.get();

        if (usuario.getHashSenha() != null && !usuario.getHashSenha().isEmpty())
        {
            throw new RegistrationConflictException("Este cadastro já foi finalizado. Prossiga com o login ou utilize a opção 'Esqueci minha senha'.");
        }

        return new PreCadastroInfoDTO(usuario.getId(), usuario.getNomeCompleto(), usuario.getEmail());
    }

    @Override
    public AuthResponseDTO finalizarCadastro(FinalizarCadastroDTO request)
    {
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId()).orElseThrow(() -> new UserNotFoundException("Usuário não encontrado."));

        if (chavePixRepository.findByUsuarioIdUsuarios(usuario).isEmpty())
        {
            throw new BadCredentialsException("É necessário cadastrar ao menos uma chave PIX para finalizar o cadastro.");
        }

        usuario.setHashSenha(passwordEncoder.encode(request.getSenha()));
        usuarioRepository.save(usuario);

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(usuario.getEmail(), request.getSenha()));

        String jwtToken = jwtService.generateToken(usuario);

        return AuthResponseDTO.builder().token(jwtToken).usuarioId(usuario.getId()).nomeUsuario(usuario.getNomeCompleto()).build();
    }

    private Optional<Usuario> buscarUsuarioPorPreCadastro(PreCadastroCheckDTO data)
    {
        if (data.getCpf() != null && !data.getCpf().trim().isEmpty())
        {
            return usuarioRepository.findByCpf(data.getCpf());
        }
        else if (data.getEmail() != null && !data.getEmail().trim().isEmpty() && data.getDataNascimento() != null)
        {
            return usuarioRepository.findByEmailAndDataNascimento(data.getEmail(), data.getDataNascimento());
        }
        return Optional.empty();
    }
}