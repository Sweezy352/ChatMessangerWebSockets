package com.example.webchatapplication.repository;

import com.example.webchatapplication.entity.AttachmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AttachmentRepository extends JpaRepository<AttachmentEntity, Long> {
    Optional<AttachmentEntity> findByOriginalFileName(String originalFileName);
}
