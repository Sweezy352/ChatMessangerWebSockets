package com.example.webchatapplication.services;

import com.example.webchatapplication.entity.PfpPictureEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface ProfilePictureService {
    InputStream streamFile(String bucketName, Long pictureId);
    PfpPictureEntity upload(MultipartFile multipartFile);
    void deleteFile(String bucketName, Long pictureId);
    PfpPictureEntity getById(Long id);
}
