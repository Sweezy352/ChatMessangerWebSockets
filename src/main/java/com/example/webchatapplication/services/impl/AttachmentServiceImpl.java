package com.example.webchatapplication.services.impl;

import com.example.webchatapplication.entity.AttachmentEntity;
import com.example.webchatapplication.exception.AttachmentNotFoundException;
import com.example.webchatapplication.repository.AttachmentRepository;
import com.example.webchatapplication.services.AttachmentService;
import com.example.webchatapplication.services.MinIoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:minio.properties")
public class AttachmentServiceImpl implements AttachmentService {
    private final MinIoService minIoService;
    private final AttachmentRepository attachmentRepository;
    @Value("${minio.bucket.name.attachments}")
    private String bucketName;

    @Override
    public InputStream streamFile(Long fileId) {
        AttachmentEntity attachmentEntity = attachmentRepository.findById(fileId).orElseThrow(() -> new AttachmentNotFoundException("Attachment not found"));
        return minIoService.streamFile(bucketName, attachmentEntity.getOriginalFileName());
    }

    @Override
    public AttachmentEntity upload(MultipartFile multipartFile) {
        minIoService.upload(multipartFile, bucketName);
        return attachmentRepository.save(AttachmentEntity.builder().originalFileName(multipartFile.getOriginalFilename()).mimeType(multipartFile.getContentType()).size(multipartFile.getSize()).build());
    }

    @Override
    public void deleteFile(String fileName) {
        minIoService.deleteFile(bucketName, fileName);
    }
}
