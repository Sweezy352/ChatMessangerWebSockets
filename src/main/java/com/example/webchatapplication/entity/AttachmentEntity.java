package com.example.webchatapplication.entity;

import com.example.webchatapplication.enums.FileType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "attachments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class AttachmentEntity extends BaseEntity{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id", referencedColumnName = "id")
    private MessageEntity messageEntity;
    @Column(name = "file_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private FileType fileType;
    @Column(name = "original_file_name", nullable = false)
    private String originalFileName;
    @Column(nullable = false)
    private Long size;
    @Column(name = "mime_type", nullable = false)
    private String mimeType;

}
