package com.poster.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
public class CommentDto {
    private Long id;
    private String text;
    private LocalDateTime date;
    private UserShortInfo user;
    private Long postId;
}
