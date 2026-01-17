package com.example.webchatapplication.services.impl;

import com.example.webchatapplication.services.MinIoService;
import io.minio.*;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
public class MinIoServiceImpl implements MinIoService {
    private final MinioClient minioClient;

    @Override
    public InputStream streamFile(String bucketName, String fileName) {
        try {
            ensureBucketExists(bucketName);
            return minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(fileName).build());
        }catch (Exception e){
            throw new RuntimeException(e);

        }

    }

    @Override
    public String getContentType(String bucketName, String fileName) {
        return "";
    }

    @Override
    public void upload(MultipartFile multipartFile, String bucketName) {
        try{
            ensureBucketExists(bucketName);
            minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(multipartFile.getOriginalFilename()).stream(multipartFile.getInputStream(), multipartFile.getSize(), -1).contentType(multipartFile.getContentType()).build());
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean fileExists(String bucketName, String fileName) {
        try {
            minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(fileName).build());
            return true;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteFile(String bucketName, String fileName) {
        try{
            minioClient.deleteObjectTags(DeleteObjectTagsArgs.builder().bucket(bucketName).object(fileName).build());
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private void ensureBucketExists(String bucketName){
        try{
            minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }
}
