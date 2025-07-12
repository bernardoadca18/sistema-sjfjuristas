package com.sjfjuristas.plataforma.backend.service;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sjfjuristas.plataforma.backend.domain.ComprovantePagamento;
import com.sjfjuristas.plataforma.backend.domain.Emprestimo;
import com.sjfjuristas.plataforma.backend.domain.PagamentoParcela;
import com.sjfjuristas.plataforma.backend.domain.ParcelaEmprestimo;
import com.sjfjuristas.plataforma.backend.dto.PagamentosParcela.PagamentoParcelaResponseDTO;
import com.sjfjuristas.plataforma.backend.repository.ComprovantePagamentoRepository;
import com.sjfjuristas.plataforma.backend.repository.EmprestimoRepository;
import com.sjfjuristas.plataforma.backend.repository.PagamentoParcelaRepository;
import com.sjfjuristas.plataforma.backend.repository.ParcelaEmprestimoRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PagamentoService
{
    @Autowired
    private ComprovantePagamentoRepository comprovanteRepository;

    @Autowired
    private ParcelaEmprestimoRepository parcelaRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private PagamentoParcelaRepository pagamentoParcelaRepository;

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    @Value("${minio.bucket.comprovantes}")
    private String comprovanteBucketName;
    
    @Transactional
    public void anexarComprovante(UUID parcelaId, UUID usuarioId, MultipartFile arquivo) throws IOException 
    {
        ParcelaEmprestimo parcela = parcelaRepository.findById(parcelaId).orElseThrow(() -> new EntityNotFoundException("Parcela não encontrada."));

        if (!parcela.getEmprestimoIdEmprestimos().getUsuarioIdUsuarios().getId().equals(usuarioId))
        {
            throw new org.springframework.security.access.AccessDeniedException("Acesso negado a esta parcela.");
        }
        
        String subfolder = "emprestimo-" + parcela.getEmprestimoIdEmprestimos().getId().toString() + "/parcela-" + parcelaId.toString();
        String fileUrl = fileStorageService.uploadFile(comprovanteBucketName, arquivo, subfolder);

        ComprovantePagamento comprovante = new ComprovantePagamento();
        comprovante.setParcelaIdParcelasemprestimo(parcela);
        comprovante.setUsuarioIdUsuarios(parcela.getEmprestimoIdEmprestimos().getUsuarioIdUsuarios());
        comprovante.setUrlComprovante(fileUrl);
        comprovante.setNomeArquivoOriginal(arquivo.getOriginalFilename());
        comprovante.setTipoMime(arquivo.getContentType());
        comprovante.setTamanhoBytes(arquivo.getSize());
        comprovante.setDataUpload(OffsetDateTime.now());
        comprovante.setStatusVerificacao("Pendente");
        
        comprovanteRepository.save(comprovante);

        // TODO: Criar uma notificação para o Admin sobre o novo comprovante.
    }

    @Transactional(readOnly = true)
    public Page<PagamentoParcelaResponseDTO> getPagamentosPorEmprestimo(UUID emprestimoId, Pageable pageable)
    {   
        Emprestimo emprestimo = emprestimoRepository.findById(emprestimoId).orElseThrow(() -> new EntityNotFoundException("Empréstimo não encontrado."));
        Page<PagamentoParcela> pagamentosPage = pagamentoParcelaRepository.findByEmprestimoIdEmprestimos(emprestimo, pageable);
        return pagamentosPage.map(PagamentoParcelaResponseDTO::new);
    }

    public void validarPropriedadeEmprestimo(UUID emprestimoId)
    {
        String emailUsuarioLogado = SecurityContextHolder.getContext().getAuthentication().getName();
        Emprestimo emprestimo = emprestimoRepository.findById(emprestimoId).orElseThrow(() -> new EntityNotFoundException("Empréstimo não encontrado com o ID: " + emprestimoId));
        
        if (!emprestimo.getUsuarioIdUsuarios().getEmail().equals(emailUsuarioLogado))
        {
            throw new AccessDeniedException("Acesso negado. Este empréstimo não pertence ao usuário autenticado.");
        }
    }
}