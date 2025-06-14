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
public class FileStorageService {

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucket.name}")
    private String bucketName;

    public String uploadFile(MultipartFile file, String subfolder) {
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }

            String fileName = subfolder + "/" + UUID.randomUUID() + "-" + file.getOriginalFilename();
            minioClient.putObject(
                    PutObjectArgs.builder().bucket(bucketName).object(fileName).stream(
                            file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build());

            return minioClient.getPresignedObjectUrl(null);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao fazer upload do arquivo: " + e.getMessage());
        }
    }
}