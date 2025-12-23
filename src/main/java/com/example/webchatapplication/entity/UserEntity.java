package com.example.webchatapplication.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserEntity extends BaseEntity{
    @Column(unique = true, nullable = false)
    private String username;
    @Column(name = "phone_number", unique = true, nullable = false)
    private String phoneNumber;
    @Column(unique = true, nullable = false)
    private String mail;
    @Column(nullable = false)
    private String password;
    @Column
    private String bio;
    @Column(nullable = false)
    private Integer age;
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;
    @Column(name = "date_registered")
    private LocalDate dateRegistered;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "userEntity")
    private List<PfpPictureEntity> pfpPictureEntities;
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "userEntities")
    private List<ChatRoomEntity> chatRoomEntities;

    @PrePersist
    public void prePersist(){
        this.dateRegistered = LocalDate.now();
    }
}
