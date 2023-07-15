package com.poster.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ChatShortDto {
    private Long id;
    private UserShortInfo firstUser;
    private UserShortInfo secondUser;
}
