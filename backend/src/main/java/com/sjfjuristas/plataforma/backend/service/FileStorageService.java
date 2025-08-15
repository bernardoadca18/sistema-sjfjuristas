package com.sjfjuristas.plataforma.backend.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
@Service
public class FileStorageService
{
    @Autowired
    private S3Client s3Client;

    @Value("${aws.s3.url}")
    private String s3Url;

    public String uploadFile(String bucketName, MultipartFile file, String subfolder) 
    {
        try 
        {
            String folderPath = (subfolder != null && !subfolder.isBlank()) ? subfolder + "/" : "";
            String fileName = folderPath + UUID.randomUUID() + "-" + file.getOriginalFilename();

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType(file.getContentType())
                .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            return s3Url + "/" + bucketName + "/" + fileName;
        } 
        catch (Exception e)
        {
            throw new RuntimeException("Erro ao fazer upload do arquivo para o bucket " + bucketName + ": " + e.getMessage(), e);
        }
    }
}