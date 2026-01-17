package com.example.webchatapplication.services;

import com.example.webchatapplication.entity.AttachmentEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface AttachmentService {
    InputStream streamFile(Long fileId);
    AttachmentEntity upload(MultipartFile multipartFile);
    void deleteFile(String fileName);

}
