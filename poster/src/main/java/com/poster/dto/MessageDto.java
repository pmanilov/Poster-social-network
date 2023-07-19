package com.poster.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
public class MessageDto {
    private Long id;
    private String text;
    private String date;
    private UserShortInfo sender;
}
