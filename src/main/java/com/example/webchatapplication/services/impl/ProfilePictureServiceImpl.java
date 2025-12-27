package com.example.webchatapplication.services.impl;

import com.example.webchatapplication.entity.PfpPictureEntity;
import com.example.webchatapplication.exception.ProfilePictureNotFound;
import com.example.webchatapplication.repository.PfpPictureRepository;
import com.example.webchatapplication.services.AuthService;
import com.example.webchatapplication.services.MinIoService;
import com.example.webchatapplication.services.ProfilePictureService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:minio.properties")
public class ProfilePictureServiceImpl implements ProfilePictureService {
    private final MinIoService minIoService;
    private final PfpPictureRepository pfpPictureRepository;
    private final AuthService authService;
    @Value("${minio.bucket.name.profile-pictures}")
    private String bucketName;

    @Override
    public InputStream streamFile(String bucketName, Long pictureId) {
        PfpPictureEntity pfpPictureEntity = pfpPictureRepository.findById(pictureId).orElseThrow(() -> new ProfilePictureNotFound("Picture not found"));
        return minIoService.streamFile(bucketName, pfpPictureEntity.getOriginalFileName());
    }

    @Override
    public PfpPictureEntity upload(MultipartFile multipartFile) {
        minIoService.upload(multipartFile, bucketName);
        return pfpPictureRepository.save(PfpPictureEntity.builder().originalFileName(multipartFile.getOriginalFilename()).mimeType(multipartFile.getContentType()).size(multipartFile.getSize()).userEntity(authService.getCurrentAuthenticated()).build());
    }

    @Override
    public void deleteFile(String bucketName, Long pictureId) {
        PfpPictureEntity pfpPictureEntity = pfpPictureRepository.findById(pictureId).orElseThrow(() -> new ProfilePictureNotFound("Picture not found"));
        minIoService.deleteFile(bucketName, pfpPictureEntity.getOriginalFileName());
        pfpPictureRepository.delete(pfpPictureEntity);
    }

    @Override
    public PfpPictureEntity getById(Long id) {
        return pfpPictureRepository.findById(id).orElseThrow(() -> new ProfilePictureNotFound("Picture not found"));
    }
}
