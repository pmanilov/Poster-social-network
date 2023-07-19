package com.poster.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@Builder
public class ImageDto {

    private String originalFileName;
    private String contentType;
    private byte[] imageData;
}
