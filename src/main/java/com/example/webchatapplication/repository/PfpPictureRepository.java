package com.example.webchatapplication.repository;

import com.example.webchatapplication.entity.PfpPictureEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PfpPictureRepository extends JpaRepository<PfpPictureEntity, Long> {
    Optional<List<PfpPictureEntity>> findAllByUserEntityId(Long userId);
}
