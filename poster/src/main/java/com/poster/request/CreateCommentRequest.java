package com.poster.request;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateCommentRequest {
    private Long postId;
    private String text;
}
