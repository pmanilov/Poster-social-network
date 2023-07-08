package com.poster.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class UserShortInfo {
    private Long id;
    private String username;
}