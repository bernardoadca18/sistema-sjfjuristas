package com.sjfjuristas.plataforma.backend.service;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;

@Service
public class FileStorageService
{
    @Autowired
    private MinioClient minioClient;

    @Value("${minio.url}")
    private String minioUrl;

    public String uploadFile(String bucketName, MultipartFile file, String subfolder) 
    {
        try 
        {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) 
            {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }

            String folderPath = (subfolder != null && !subfolder.isBlank()) ? subfolder + "/" : "";
            String fileName = folderPath + UUID.randomUUID() + "-" + file.getOriginalFilename();

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build());

            return minioUrl + "/" + bucketName + "/" + fileName;

        } 
        catch (Exception e)
        {
            throw new RuntimeException("Erro ao fazer upload do arquivo para o bucket " + bucketName + ": " + e.getMessage(), e);
        }
    }
}