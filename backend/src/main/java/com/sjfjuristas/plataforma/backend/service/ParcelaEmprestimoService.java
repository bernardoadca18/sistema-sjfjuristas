package com.sjfjuristas.plataforma.backend.service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.sjfjuristas.plataforma.backend.domain.ParcelaEmprestimo;
import com.sjfjuristas.plataforma.backend.repository.ParcelaEmprestimoRepository;
import com.sjfjuristas.plataforma.backend.util.ByteArrayMultipartFile;
import jakarta.transaction.Transactional;

@Service
public class ParcelaEmprestimoService 
{
    @Autowired
    private PixService pixService;

    @Autowired
    private ParcelaEmprestimoRepository parcelaRepository;

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
    public ParcelaEmprestimo gerarPixParaParcela(UUID parcelaId)
    {
        ParcelaEmprestimo parcela = parcelaRepository.findById(parcelaId).orElseThrow(() -> new RuntimeException("Parcela não encontrada"));
        String descricao = "Pagamento da parcela " + parcela.getNumeroParcela();
        BigDecimal valorParcela =  parcela.getValorTotalParcela();

        return gerarEPersistirPix(parcela, valorParcela, descricao);
    }

    private ParcelaEmprestimo gerarEPersistirPix(ParcelaEmprestimo parcela, BigDecimal valor, String descricao) {
        PixService.PixResult pixResult = pixService.gerarPix(
                chavePixEmpresa,
                valor,
                nomeEmpresa,
                cidadeEmpresa,
                descricao
        );

        ByteArrayMultipartFile qrCodeFile = new ByteArrayMultipartFile(
                pixResult.qrCode(),
                "qrcode_parcela_" + parcela.getId() + ".png",
                "image/png"
        );
        String subfolder = "qrcodes/parcela-" + parcela.getId();
        String qrCodeUrl = fileStorageService.uploadFile(qrCodeBucketName, qrCodeFile, subfolder);

        OffsetDateTime agora = OffsetDateTime.now();
        OffsetDateTime dataExpiracao = agora.plusSeconds(expiracaoSegundos);

        parcela.setPixCopiaCola(pixResult.payload());
        parcela.setPixQrCodeBase64(qrCodeUrl);
        parcela.setDataGeracaoPix(agora);
        parcela.setPixDataExpiracao(dataExpiracao);
        
        return parcelaRepository.save(parcela);
    }
}
