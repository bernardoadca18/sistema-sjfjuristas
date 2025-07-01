package com.sjfjuristas.plataforma.backend.service.CRUD;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sjfjuristas.plataforma.backend.domain.Ocupacao;
import com.sjfjuristas.plataforma.backend.domain.PropostaEmprestimo;
import com.sjfjuristas.plataforma.backend.domain.Usuario;
import com.sjfjuristas.plataforma.backend.dto.CRUD.OcupacaoResponseDTO;
import com.sjfjuristas.plataforma.backend.dto.CRUD.UsuarioResponseDTO;
import com.sjfjuristas.plataforma.backend.repository.PropostaEmprestimoRepository;
import com.sjfjuristas.plataforma.backend.repository.UsuarioRepository;
import com.sjfjuristas.plataforma.backend.repository.spec.UsuarioSpecification;

@Service
public class UsuarioCRUDService
{
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PropostaEmprestimoRepository propostaEmprestimoRepository;

    @Transactional(readOnly = true)
    public Page<UsuarioResponseDTO> getAllUsuarios(Pageable pageable)
    {
        Page<Usuario> usuarios = usuarioRepository.findAll(pageable);
        return usuarios.map(this::toResponseDto);
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDTO getUsuarioById(UUID id)
    {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return toResponseDto(usuario);
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDTO getUsuarioByEmail(String email)
    {
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return toResponseDto(usuario);
    }

    public Page<UsuarioResponseDTO> searchUsuarios(String busca, Pageable pageable)
    {
        Specification<Usuario> spec = UsuarioSpecification.searchUsuario(busca);

        Page<Usuario> paginaUsuarios = usuarioRepository.findAll(spec, pageable);

        return paginaUsuarios.map(this::toResponseDto);
    }

    private UsuarioResponseDTO toResponseDto(Usuario usuario)
    {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();

        //BigDecimal rendaMensal = propostaEmprestimoRepository.findTopByUsuarioIdOrderByDataPropostaDesc(usuario.getId()).map(PropostaEmprestimo::getRemuneracaoMensalSolicitante).orElseThrow(() -> new RuntimeException("Valor não encontrado"));
        //Ocupacao ocupacao = propostaEmprestimoRepository.findTopByUsuarioIdOrderByDataPropostaDesc(usuario.getId()).map(PropostaEmprestimo::getOcupacao).orElseThrow(() -> new RuntimeException("Valor não encontrado"));
        BigDecimal rendaMensal = propostaEmprestimoRepository.findTopByUsuarioIdUsuarios_IdOrderByDataSolicitacaoDesc(usuario.getId()).map(PropostaEmprestimo::getRemuneracaoMensalSolicitante).orElseThrow(() -> new RuntimeException("Valor não encontrado"));
        Ocupacao ocupacao = propostaEmprestimoRepository.findTopByUsuarioIdUsuarios_IdOrderByDataSolicitacaoDesc(usuario.getId()).map(PropostaEmprestimo::getOcupacao).orElseThrow(() -> new RuntimeException("Valor não encontrado"));
        
        OcupacaoResponseDTO ocupacaoDto = new OcupacaoResponseDTO(ocupacao);

        dto.setId(usuario.getId());
        dto.setNome(usuario.getNomeCompleto());
        dto.setEmail(usuario.getEmail());
        dto.setTelefone(usuario.getTelefoneWhatsapp());
        dto.setCpf(usuario.getCpf());
        dto.setDataNascimento(usuario.getDataNascimento());
        dto.setRendaMensal(rendaMensal);
        dto.setOcupacao(ocupacaoDto);
        dto.setAtivo(usuario.getAtivo());
        dto.setDataInclusao(usuario.getDataCadastro().toLocalDateTime());
        dto.setDataAlteracao(LocalDateTime.now());

        return dto;
    }
}
