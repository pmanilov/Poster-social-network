package com.poster.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class UserDto {
    private Long id;
    private String username;
    private String about;

    private int amountOfFollowers;
    private int amountOfFollowing;
    private int amountOfPosts;
}
