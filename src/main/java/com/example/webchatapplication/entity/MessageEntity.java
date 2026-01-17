package com.example.webchatapplication.entity;

import com.example.webchatapplication.enums.MessageStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class MessageEntity extends BaseEntity{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", referencedColumnName = "id")
    private ChatRoomEntity chatRoomEntity;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private UserEntity sender;
    @Column(name = "content", nullable = false)
    private String content;
    @Column(name = "date_sent")
    private LocalDateTime dateSent;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private MessageStatus messageStatus;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "messageEntity")
    private List<AttachmentEntity> attachmentEntities;

    @PrePersist
    public void prePersist(){
        this.dateSent = LocalDateTime.now();
        this.messageStatus = MessageStatus.SEND;
    }
}
