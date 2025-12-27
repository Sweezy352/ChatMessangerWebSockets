package com.example.webchatapplication.controller;

import com.example.webchatapplication.entity.PfpPictureEntity;
import com.example.webchatapplication.exception.BaseException;
import com.example.webchatapplication.services.ProfilePictureService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@RestController
@RequestMapping("/api/profile-pictures")
@RequiredArgsConstructor
public class ProfilePictureController {
    private final ProfilePictureService profilePictureService;

    @PostMapping("/upload")
    public void uploadProfilePicture(@RequestParam("file") MultipartFile multipartFile) throws BaseException{
        profilePictureService.upload(multipartFile);
    }

    @DeleteMapping("/delete")
    public void deleteProfilePicture(@RequestParam("pictureId") Long pictureId) throws BaseException{
        profilePictureService.deleteFile("profile-pictures", pictureId);
    }

    @GetMapping("/stream")
    public ResponseEntity<InputStreamResource> streamProfilePicture(@RequestParam("pictureId") Long pictureId) throws BaseException {
        PfpPictureEntity pfpPictureEntity = profilePictureService.getById(pictureId);
        InputStream inputStream = profilePictureService.streamFile("profile-pictures", pfpPictureEntity.getId());
        return ResponseEntity.ok().contentType(MediaType.valueOf(pfpPictureEntity.getMimeType())).body(new InputStreamResource(inputStream));
    }
}
