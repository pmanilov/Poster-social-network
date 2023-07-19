package com.poster.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "post_image")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostImage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String originalFileName;
    private String contentType;
    @Lob
    private byte[] imageData;

    @OneToOne
    @JoinColumn(name = "post_id")
    private Post post;

}
