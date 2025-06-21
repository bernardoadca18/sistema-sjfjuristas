package com.sjfjuristas.plataforma.backend.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.stereotype.Service;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;

@Service
public class PixService 
{
    public PixResult gerarPix(String chavePix, BigDecimal valor, String nomeBeneficiario, String cidadeBeneficiario, String descricao) 
    {
        String payload = gerarPayload(chavePix, valor, nomeBeneficiario, cidadeBeneficiario, descricao);
        byte[] qrCodeBytes = gerarQrCodeImageBytes(payload);

        return new PixResult(payload, qrCodeBytes);
    }

    private String gerarPayload(String chavePix, BigDecimal valor, String nome, String cidade, String descricao) 
    {
        if (nome.length() > 25) nome = nome.substring(0, 25);
        if (cidade.length() > 15) cidade = cidade.substring(0, 15);

        StringBuilder payload = new StringBuilder();
        payload.append(formatarCampo("00", "01"));
        payload.append(formatarCampo("26",
                formatarCampo("00", "BR.GOV.BCB.PIX") +
                formatarCampo("01", chavePix) +
                (descricao != null && !descricao.isBlank() ? formatarCampo("02", descricao) : "")
        ));
        payload.append(formatarCampo("52", "0000"));
        payload.append(formatarCampo("53", "986"));
        payload.append(formatarCampo("54", String.format("%.2f", valor).replace(",", ".")));
        payload.append(formatarCampo("58", "BR"));
        payload.append(formatarCampo("59", nome));
        payload.append(formatarCampo("60", cidade));
        payload.append(formatarCampo("62", formatarCampo("05", "***")));
        payload.append("6304");

        String crc16 = calcularCRC16(payload.toString());
        payload.append(crc16);

        return payload.toString();
    }

    private byte[] gerarQrCodeImageBytes(String payload) 
    {
        try 
        {
            Map<EncodeHintType, Object> hintMap = new EnumMap<>(EncodeHintType.class);
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hintMap.put(EncodeHintType.MARGIN, 1);

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(payload, BarcodeFormat.QR_CODE, 300, 300, hintMap);

            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (int y = 0; y < height; y++) 
            {
                for (int x = 0; x < width; x++) 
                {
                    image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "PNG", baos);
            return baos.toByteArray();

        } 
        catch (WriterException | IOException e) 
        {
            throw new RuntimeException("Erro ao gerar a imagem do QR Code", e);
        }
    }

    private String formatarCampo(String id, String valor) 
    {
        String tamanho = String.format("%02d", valor.length());
        return id + tamanho + valor;
    }

    private String calcularCRC16(String data) 
    {
        int crc = 0xFFFF;
        int polynomial = 0x1021;

        byte[] bytes = data.getBytes();

        for (byte b : bytes) 
        {
            for (int i = 0; i < 8; i++) 
            {
                boolean bit = ((b >> (7 - i) & 1) == 1);
                boolean c15 = ((crc >> 15 & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit) {
                    crc ^= polynomial;
                }
            }
        }
        crc &= 0xFFFF;
        return String.format("%04X", crc);
    }

    public record PixResult(String payload, byte[] qrCode) {}
}