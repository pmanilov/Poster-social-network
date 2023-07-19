package com.poster.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_image")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserImage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String originalFileName;
    private String contentType;
    @Lob
    private byte[] imageData;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
