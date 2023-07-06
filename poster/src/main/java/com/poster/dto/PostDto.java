package com.poster.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Setter
@Getter
@Builder
public class PostDto {
    private Long id;
    private String text;
    private LocalDateTime date;
    private UserShortInfo user;
    private Set<UserShortInfo> likedBy;
    private Integer amountOfLikes;
    private Integer amountOfComments;
}
