package com.sjfjuristas.plataforma.backend.service.impl;

import com.sjfjuristas.plataforma.backend.domain.Administrador;
import com.sjfjuristas.plataforma.backend.domain.PerfilUsuario;
import com.sjfjuristas.plataforma.backend.dto.Admin.AdminCreateRequestDTO;
import com.sjfjuristas.plataforma.backend.dto.Admin.AdminResponseDTO;
import com.sjfjuristas.plataforma.backend.dto.Admin.AdminUpdateRequestDTO;
import com.sjfjuristas.plataforma.backend.repository.AdministradorRepository;
import com.sjfjuristas.plataforma.backend.repository.PerfilUsuarioRepository;
import com.sjfjuristas.plataforma.backend.service.AdministradorService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdministradorServiceImpl implements AdministradorService {

    private static final Logger logger = LoggerFactory.getLogger(AdministradorServiceImpl.class);

    private final AdministradorRepository administradorRepository;
    private final PerfilUsuarioRepository perfilUsuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public AdminResponseDTO criarAdministrador(AdminCreateRequestDTO request) {
        if (administradorRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email já cadastrado para um administrador.");
        }
        if (request.getMatriculaFuncionario() != null && administradorRepository.existsByMatriculaFuncionario(request.getMatriculaFuncionario())) {
            throw new IllegalArgumentException("Matrícula de funcionário já cadastrada.");
        }

        Administrador admin = new Administrador();
        admin.setNomeCompleto(request.getNomeCompleto());
        admin.setEmail(request.getEmail());
        admin.setHashSenha(passwordEncoder.encode(request.getSenha()));
        admin.setTelefoneContato(request.getTelefoneContatoInterno());
        admin.setCargoInterno(request.getCargoInterno());
        admin.setDepartamento(request.getDepartamento());
        admin.setMatriculaFuncionario(request.getMatriculaFuncionario());
        admin.setAtivo(true); // Default true

        PerfilUsuario perfilAdmin = perfilUsuarioRepository.findById(request.getPerfilId())
                .orElseThrow(() -> new EntityNotFoundException("Perfil de administrador não encontrado com ID: " + request.getPerfilId()));

        admin.setPerfilIdPerfisusuario(perfilAdmin.getId());

        Administrador savedAdmin = administradorRepository.save(admin);
        logger.info("Administrador {} criado com ID: {}", savedAdmin.getNomeCompleto(), savedAdmin.getId());
        return mapToAdminResponseDTO(savedAdmin);
    }

    @Override
    @Transactional(readOnly = true)
    public AdminResponseDTO getAdministradorById(UUID adminId) {
        Administrador admin = administradorRepository.findById(adminId)
                .orElseThrow(() -> new EntityNotFoundException("Administrador não encontrado com ID: " + adminId));
        return mapToAdminResponseDTO(admin);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AdminResponseDTO> getAllAdministradores(Pageable pageable) {
        return administradorRepository.findAll(pageable).map(this::mapToAdminResponseDTO);
    }

    @Override
    @Transactional
    public AdminResponseDTO updateAdministrador(UUID adminId, AdminUpdateRequestDTO request) {
        Administrador admin = administradorRepository.findById(adminId)
                .orElseThrow(() -> new EntityNotFoundException("Administrador não encontrado com ID: " + adminId));

        if (request.getNomeCompleto() != null) admin.setNomeCompleto(request.getNomeCompleto());
        if (request.getTelefoneContatoInterno() != null) admin.setTelefoneContato(request.getTelefoneContatoInterno());
        if (request.getCargoInterno() != null) admin.setCargoInterno(request.getCargoInterno());
        if (request.getDepartamento() != null) admin.setDepartamento(request.getDepartamento());
        if (request.getMatriculaFuncionario() != null) {

            if (!admin.getMatriculaFuncionario().equals(request.getMatriculaFuncionario()) &&
                administradorRepository.existsByMatriculaFuncionario(request.getMatriculaFuncionario())) {
                throw new IllegalArgumentException("Nova matrícula de funcionário já cadastrada.");
            }
            admin.setMatriculaFuncionario(request.getMatriculaFuncionario());
        }
        admin.setAtivo(true);
        if (request.getPerfilId() != null) {
            PerfilUsuario novoPerfil = perfilUsuarioRepository.findById(request.getPerfilId())
                    .orElseThrow(() -> new EntityNotFoundException("Novo perfil de administrador não encontrado com ID: " + request.getPerfilId()));
            admin.setPerfilIdPerfisusuario(novoPerfil.getId());
        }

        Administrador updatedAdmin = administradorRepository.save(admin);
        return mapToAdminResponseDTO(updatedAdmin);
    }

    @Override
    @Transactional
    public void deleteAdministrador(UUID adminId) {
        if (!administradorRepository.existsById(adminId)) {
            throw new EntityNotFoundException("Administrador não encontrado com ID: " + adminId);
        }
        administradorRepository.deleteById(adminId);
        logger.info("Administrador com ID: {} deletado.", adminId);
    }

    private AdminResponseDTO mapToAdminResponseDTO(Administrador admin) {
        return AdminResponseDTO.builder()
                .nomeCompleto(admin.getNomeCompleto())
                .email(admin.getEmail())
                .telefoneContatoInterno(admin.getTelefoneContato())
                .perfilName("ADMINISTRADOR")
                .cargoInterno(admin.getCargoInterno())
                .departamento(admin.getDepartamento())
                .matriculaFuncionario(admin.getMatriculaFuncionario())
                .ativo(Boolean.TRUE.equals(admin.getAtivo()))
                .build();
    }
}