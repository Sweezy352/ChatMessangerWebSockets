package com.example.webchatapplication.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "pfp_pictures_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PfpPictureEntity extends BaseEntity{
    @Column(name = "original_file_name", nullable = false)
    private String originalFileName;
    @Column(name = "mime_type", nullable = false)
    private String mimeType;
    @Column(name = "size", nullable = false)
    private String size;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity userEntity;
}
