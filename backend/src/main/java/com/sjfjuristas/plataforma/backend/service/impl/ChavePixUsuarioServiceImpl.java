package com.sjfjuristas.plataforma.backend.service.impl;

import com.sjfjuristas.plataforma.backend.domain.ChavePixUsuario;
import com.sjfjuristas.plataforma.backend.domain.TipoChavePix;
import com.sjfjuristas.plataforma.backend.domain.Usuario;
import com.sjfjuristas.plataforma.backend.dto.ChavesPixUsuario.ChavePixCreateRequestDTO;
import com.sjfjuristas.plataforma.backend.dto.ChavesPixUsuario.ChavePixResponseDTO;
import com.sjfjuristas.plataforma.backend.dto.ChavesPixUsuario.ChavePixUpdateRequestDTO;
import com.sjfjuristas.plataforma.backend.repository.ChavePixUsuarioRepository;
import com.sjfjuristas.plataforma.backend.repository.TipoChavePixRepository;
import com.sjfjuristas.plataforma.backend.repository.UsuarioRepository;
import com.sjfjuristas.plataforma.backend.service.ChavePixUsuarioService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChavePixUsuarioServiceImpl implements ChavePixUsuarioService {

    private final ChavePixUsuarioRepository chavePixRepository;
    private final UsuarioRepository usuarioRepository;
    private final TipoChavePixRepository tipoChavePixRepository;

    @Override
    @Transactional
    public ChavePixResponseDTO adicionarChavePix(UUID usuarioId, ChavePixCreateRequestDTO request) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado: " + usuarioId));
        
        TipoChavePix tipoChave = tipoChavePixRepository.findById(request.getTipoChavePixId())
                .orElseThrow(() -> new EntityNotFoundException("Tipo de Chave PIX não encontrado: " + request.getTipoChavePixId()));

        // TODO: Validar se já existe essa chave para o usuário

        ChavePixUsuario novaChave = new ChavePixUsuario();
        novaChave.setUsuarioIdUsuarios(usuario);
        novaChave.setTipoChavePixIdTiposchavepix(tipoChave);
        novaChave.setValorChave(request.getValorChave());
        novaChave.setAtivaParaDesembolso(false);

        ChavePixUsuario savedChave = chavePixRepository.save(novaChave);
        return mapToResponseDTO(savedChave);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChavePixResponseDTO> getChavesPixPorUsuario(UUID usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado: " + usuarioId));
        return chavePixRepository.findByUsuarioIdUsuarios(usuario) // Ajuste nome do método
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ChavePixResponseDTO getChavePixById(UUID chavePixId) {
        ChavePixUsuario chave = chavePixRepository.findById(chavePixId)
            .orElseThrow(() -> new EntityNotFoundException("Chave PIX não encontrada: " + chavePixId));
        return mapToResponseDTO(chave);
    }

    @Override
    @Transactional
    public ChavePixResponseDTO updateChavePix(UUID chavePixId, ChavePixUpdateRequestDTO request) {
        ChavePixUsuario chave = chavePixRepository.findById(chavePixId)
            .orElseThrow(() -> new EntityNotFoundException("Chave PIX não encontrada: " + chavePixId));
        
        
        if (request.getAtivaParaDesembolso() != null) {
            // Se for tornar esta chave ativa, desativar outras do mesmo usuário
            if (Boolean.TRUE.equals(request.getAtivaParaDesembolso())) {
                List<ChavePixUsuario> outrasChaves = chavePixRepository.findByUsuarioIdUsuarios(chave.getUsuarioIdUsuarios());
                outrasChaves.forEach(outra -> {
                    if (!outra.getId().equals(chavePixId)) {
                        outra.setAtivaParaDesembolso(false);
                    }
                });
                chavePixRepository.saveAll(outrasChaves); // Salvar as outras chaves desativadas
            }
            chave.setAtivaParaDesembolso(request.getAtivaParaDesembolso());
        }
        // Adicionar atualização de outros campos se permitido no DTO

        ChavePixUsuario updatedChave = chavePixRepository.save(chave);
        return mapToResponseDTO(updatedChave);
    }

    @Override
    @Transactional
    public void deleteChavePix(UUID chavePixId, UUID usuarioIdAutenticado) {
        ChavePixUsuario chave = chavePixRepository.findById(chavePixId)
            .orElseThrow(() -> new EntityNotFoundException("Chave PIX não encontrada: " + chavePixId));
        
        // Validar se o usuário autenticado é o dono da chave ou um admin
        if (!chave.getUsuarioIdUsuarios().getId().equals(usuarioIdAutenticado) /* && !usuarioAutenticadoEhAdmin() */) {
            throw new SecurityException("Usuário não autorizado a deletar esta chave PIX.");
        }
        
        // Validar se a chave não está em uso em algum empréstimo ativo como chave de desembolso
        // if (emprestimoRepository.existsByChavePixDesembolso(chave)) {
        //    throw new IllegalStateException("Chave PIX não pode ser deletada pois está em uso.");
        // }

        chavePixRepository.delete(chave);
    }

    private ChavePixResponseDTO mapToResponseDTO(ChavePixUsuario chave) {
        // Implementar mapeamento, considerar mascaramento do valor da chave
        return ChavePixResponseDTO.builder()
                .id(chave.getId())
                .tipoChavePixNome(chave.getTipoChavePixIdTiposchavepix() != null ? chave.getTipoChavePixIdTiposchavepix().getNomeTipo() : null)
                .valorChaveMascarado(mascararValorChave(chave.getValorChave(), chave.getTipoChavePixIdTiposchavepix()))
                .ativaParaDesembolso(Boolean.TRUE.equals(chave.getAtivaParaDesembolso()))
                .dataCadastro(chave.getDataCadastro())
                .build();
    }

    private String mascararValorChave(String valor, TipoChavePix tipo) {
        // Implementar lógica de mascaramento se necessário
        if (tipo != null && ("CPF".equalsIgnoreCase(tipo.getNomeTipo()) || "Telefone".equalsIgnoreCase(tipo.getNomeTipo()))) {
            // Exemplo simples
            return valor.length() > 4 ? "****" + valor.substring(valor.length() - 4) : "****";
        }
        return valor;
    }
}