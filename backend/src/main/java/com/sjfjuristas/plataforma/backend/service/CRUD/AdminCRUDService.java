package com.sjfjuristas.plataforma.backend.service.CRUD;

import com.sjfjuristas.plataforma.backend.domain.Administrador;
import com.sjfjuristas.plataforma.backend.domain.PerfilUsuario;
import com.sjfjuristas.plataforma.backend.dto.Admin.AdminCreateRequestDTO;
import com.sjfjuristas.plataforma.backend.dto.Admin.AdminResponseDTO;
import com.sjfjuristas.plataforma.backend.dto.Admin.AdminUpdateRequestDTO;
import com.sjfjuristas.plataforma.backend.repository.AdministradorRepository;
import com.sjfjuristas.plataforma.backend.repository.PerfilUsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AdminCRUDService
{
    @Autowired
    private AdministradorRepository administradorRepository;

    @Autowired
    private PerfilUsuarioRepository perfilUsuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Transactional
    public AdminResponseDTO createAdmin(AdminCreateRequestDTO request)
    {
        if (!request.getSenha().equals(request.getConfirmarSenha()))
        {
            throw new IllegalArgumentException("As senhas não coincidem.");
        }

        if (administradorRepository.existsByEmail(request.getEmail()))
        {
            throw new IllegalArgumentException("Já existe um administrador com este e-mail.");
        }

        if (administradorRepository.existsByMatriculaFuncionario(request.getMatriculaFuncionario()))
        {
            throw new IllegalArgumentException("Já existe um administrador com esta matrícula.");
        }


        Administrador novoAdministrador = new Administrador();

        PerfilUsuario perfilAdmin = perfilUsuarioRepository.findByNomePerfil("Administrador").orElseThrow(() -> new EntityNotFoundException("Perfil de administrador não encontrado."));

        novoAdministrador.setNomeCompleto(request.getNomeCompleto());
        novoAdministrador.setEmail(request.getEmail());
        novoAdministrador.setHashSenha(passwordEncoder.encode(request.getSenha()));
        novoAdministrador.setTelefoneContato(request.getTelefoneContatoInterno());
        novoAdministrador.setCargoInterno(request.getCargoInterno());
        novoAdministrador.setDepartamento(request.getDepartamento());
        novoAdministrador.setDataCadastro(OffsetDateTime.now());
        novoAdministrador.setMatriculaFuncionario(request.getMatriculaFuncionario());
        novoAdministrador.setAtivo(true);
        novoAdministrador.setTokenRecuperacaoSenha(null);
        novoAdministrador.setValidadeTokenRecuperacao(null);
        novoAdministrador.setEmailVerificado(false);
        novoAdministrador.setTokenVerificacaoEmail(null);
        novoAdministrador.setPerfilIdPerfisusuario(perfilAdmin.getId());

        Administrador administradorSalvo = administradorRepository.save(novoAdministrador);

        AdminResponseDTO adminResponseDTO = new AdminResponseDTO(administradorSalvo);

        return adminResponseDTO;
    }

    @Transactional(readOnly = true)
    public AdminResponseDTO getAdminById(UUID id)
    {
        Administrador administrador = administradorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Administrador não encontrado com o ID: " + id));
        AdminResponseDTO adminResponseDTO = new AdminResponseDTO(administrador);

        return adminResponseDTO;
    }

    @Transactional(readOnly = true)
    public Page<AdminResponseDTO> getAllAdmins(Pageable pageable)
    {
        Page<Administrador> administradores = administradorRepository.findAll(pageable);
        
        return administradores.map(AdminResponseDTO::new);
    }

    @Transactional(readOnly = true)
    public List<AdminResponseDTO> getAdminsByStatus(boolean ativo)
    {
        List<Administrador> administradores = administradorRepository.findByAtivo(ativo);

        return administradores.stream().map(AdminResponseDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AdminResponseDTO getAdminByEmail(String email)
    {
        Administrador administrador = administradorRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Administrador não encontrado com o e-mail: " + email));
        
        return new AdminResponseDTO(administrador);
    }

    @Transactional(readOnly = true)
    public AdminResponseDTO getAdminByMatriculaFuncionario(String matriculaFuncionario)
    {
        Administrador administrador = administradorRepository.findByMatriculaFuncionario(matriculaFuncionario).orElseThrow(() -> new EntityNotFoundException("Administrador não encontrado com a matrícula: " + matriculaFuncionario));

        return new AdminResponseDTO(administrador);
    }

    @Transactional(readOnly = true)
    public List<AdminResponseDTO> getAdminsByCargoInterno(String cargoInterno)
    {
        List<Administrador> administradores = administradorRepository.findByCargoInterno(cargoInterno);
    
        return administradores.stream().map(AdminResponseDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AdminResponseDTO> getAdminsByDepartamento(String departamento)
    {
        List<Administrador> administradores = administradorRepository.findByDepartamento(departamento);
        
        return administradores.stream().map(AdminResponseDTO::new).collect(Collectors.toList());
    }

    @Transactional
    public AdminResponseDTO updateAdmin(UUID id, AdminUpdateRequestDTO request)
    {
        Administrador administrador = administradorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Administrador não encontrado com o ID: " + id));

        if (request.getNomeCompleto() != null)
        {
            administrador.setNomeCompleto(request.getNomeCompleto());
        }
        if (request.getTelefoneContatoInterno() != null) {
            administrador.setTelefoneContato(request.getTelefoneContatoInterno());
        }
        if (request.getCargoInterno() != null) {
            administrador.setCargoInterno(request.getCargoInterno());
        }
        if (request.getDepartamento() != null) {
            administrador.setDepartamento(request.getDepartamento());
        }
        if (request.getMatriculaFuncionario() != null) {
            administrador.setMatriculaFuncionario(request.getMatriculaFuncionario());
        }
        if (request.isAtivo() != administrador.getAtivo())
        {
            administrador.setAtivo(request.isAtivo());
        }

        Administrador administradorAtualizado = administradorRepository.save(administrador);

        return new AdminResponseDTO(administradorAtualizado);
    }

    @Transactional
    public void deleteAdmin(UUID id)
    {
        if (!administradorRepository.existsById(id))
        {
            throw new EntityNotFoundException("Administrador não encontrado com o ID: " + id);
        }
        administradorRepository.deleteById(id);
    }
}
