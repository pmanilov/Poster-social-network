package com.poster.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class ChatDto {
    private Long id;
    private UserShortInfo firstUser;
    private UserShortInfo secondUser;
    private List<MessageDto> messages;
}
