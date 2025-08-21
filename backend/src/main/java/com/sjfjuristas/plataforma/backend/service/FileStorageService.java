package com.sjfjuristas.plataforma.backend.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService
{
    String uploadFile(String bucketName, MultipartFile file, String subfolder);
}