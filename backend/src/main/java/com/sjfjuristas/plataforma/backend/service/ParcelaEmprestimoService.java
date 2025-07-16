package com.sjfjuristas.plataforma.backend.service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sjfjuristas.plataforma.backend.domain.Emprestimo;
import com.sjfjuristas.plataforma.backend.domain.ParcelaEmprestimo;
import com.sjfjuristas.plataforma.backend.dto.ParcelaEmprestimo.ParcelaEmprestimoResponseDTO;
import com.sjfjuristas.plataforma.backend.repository.EmprestimoRepository;
import com.sjfjuristas.plataforma.backend.repository.ParcelaEmprestimoRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ParcelaEmprestimoService 
{
    @Autowired
    private PixService pixService;

    @Autowired
    private ParcelaEmprestimoRepository parcelaRepository;

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Value("${pix.cobranca.expiracao-segundos}")
    private long expiracaoSegundos;

    @Value("${minio.bucket.qrcodes}")
    private String qrCodeBucketName;

    @Value("${sjfjuristas.pix.empresa.nome}")
    private String nomeEmpresa;

    @Value("${sjfjuristas.pix.empresa.cidade}")
    private String cidadeEmpresa;

    @Value("${sjfjuristas.pix.empresa.chave}")
    private String chavePixEmpresa;

    @Transactional
    public ParcelaEmprestimoResponseDTO gerarPixParaParcela(UUID parcelaId, UUID usuarioId)
    {
        ParcelaEmprestimo parcela = parcelaRepository.findById(parcelaId).orElseThrow(() -> new RuntimeException("Parcela não encontrada"));

        if (!parcela.getEmprestimoIdEmprestimos().getUsuarioIdUsuarios().getId().equals(usuarioId))
        {
            throw new org.springframework.security.access.AccessDeniedException("Acesso negado a esta parcela.");
        }

        if (parcela.getPixCopiaCola() != null && parcela.getPixDataExpiracao().isAfter(OffsetDateTime.now()))
        {
            return new ParcelaEmprestimoResponseDTO(parcela);
        }

        String descricao = "Pagamento da parcela " + parcela.getNumeroParcela();
        BigDecimal valorParcela =  parcela.getValorTotalParcela();

        ParcelaEmprestimo parcelaEmprestimo = gerarEPersistirPix(parcela, valorParcela, descricao);

        return new ParcelaEmprestimoResponseDTO(parcelaEmprestimo);
    }


    public ParcelaEmprestimoResponseDTO getParcelaDTOByIdAndUsuario(UUID parcelaId, UUID usuarioId)
    {
        ParcelaEmprestimo parcela = parcelaRepository.findById(parcelaId).orElseThrow(() -> new EntityNotFoundException("Parcela não encontrada com o ID: " + parcelaId));

        if (!parcela.getEmprestimoIdEmprestimos().getUsuarioIdUsuarios().getId().equals(usuarioId))
        {
            throw new AccessDeniedException("Acesso negado. Esta parcela não pertence ao usuário autenticado.");
        }

        return new ParcelaEmprestimoResponseDTO(parcela);
    }

    
    @Transactional
    private ParcelaEmprestimo gerarEPersistirPix(ParcelaEmprestimo parcela, BigDecimal valor, String descricao) {
        PixService.PixResult pixResult = pixService.gerarPix(
                chavePixEmpresa,
                valor,
                nomeEmpresa,
                cidadeEmpresa,
                descricao
        );

        OffsetDateTime agora = OffsetDateTime.now();
        OffsetDateTime dataExpiracao = agora.plusSeconds(expiracaoSegundos);

        parcela.setPixCopiaCola(pixResult.payload());
        parcela.setPixQrCodeBase64(pixResult.qrCodeBase64());
        parcela.setDataGeracaoPix(agora);
        parcela.setPixDataExpiracao(dataExpiracao);
        
        return parcelaRepository.save(parcela);
    }

    @Transactional(readOnly=true)
    public Page<ParcelaEmprestimoResponseDTO> getParcelasByEmprestimoId(UUID emprestimoId, Pageable pageable) {
        Emprestimo emprestimo = emprestimoRepository.findById(emprestimoId).get();
        
        Page<ParcelaEmprestimo> parcelasPage = parcelaRepository.findByEmprestimoIdEmprestimosOrderByNumeroParcelaAsc(emprestimo, pageable);
        return parcelasPage.map(ParcelaEmprestimoResponseDTO::new);
    }

    @Transactional(readOnly=true)
    public ParcelaEmprestimoResponseDTO getProximaParcela(UUID emprestimoId) 
    {
        ParcelaEmprestimo parcela = parcelaRepository.findProximaParcelaPendente(emprestimoId, "Pendente").orElseThrow(() -> new EntityNotFoundException("Nenhuma parcela pendente encontrada para o empréstimo ID: " + emprestimoId));

        return new ParcelaEmprestimoResponseDTO(parcela);
    }
}
