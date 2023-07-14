package com.poster.model;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChatRequest {
    private Long firstUserId;
    private Long secondUserId;
}
