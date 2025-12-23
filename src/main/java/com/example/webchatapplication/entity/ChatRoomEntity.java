package com.example.webchatapplication.entity;

import com.example.webchatapplication.enums.ChatRoomType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Table(name = "chat_rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ChatRoomEntity extends BaseEntity{
    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ChatRoomType chatRoomType;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "chat_room_users", joinColumns = @JoinColumn(name = "chat_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<UserEntity> userEntities;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "chatRoomEntity")
    private List<MessageEntity> messageEntities;
}
