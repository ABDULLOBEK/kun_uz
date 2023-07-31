package com.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "email_hisotry")
public class EmailHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "message", columnDefinition = "TEXT", length = 10000)
    private String message;

    @Column(name = "created_date")
    private LocalDateTime createdDate=LocalDateTime.now();
}
