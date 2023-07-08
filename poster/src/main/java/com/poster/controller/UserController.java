package com.poster.controller;

import com.poster.dto.UserDto;
import com.poster.dto.UserShortInfo;
import com.poster.model.FollowRequest;
import com.poster.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id){
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<UserDto>> getAllUsers(){
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    //add find users method

    @GetMapping("/followers/{userId}")
    public ResponseEntity<List<UserShortInfo>> getUsersFollowers(@PathVariable Long userId){
        return new ResponseEntity<>(userService.getUserFollowers(userId), HttpStatus.OK);    }

    @GetMapping("/followed/{userId}")
    public ResponseEntity<List<UserShortInfo>> getUserFollowing(@PathVariable Long userId){
        return new ResponseEntity<>(userService.getFollowedUsers(userId), HttpStatus.OK);
    }

    @PostMapping("/subscribe")
    public ResponseEntity subscribe(@RequestBody FollowRequest followRequest){
        userService.follow(followRequest.getFollowingId());
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/unsubscribe")
    public ResponseEntity unsubscribe(@RequestBody FollowRequest followRequest){
        userService.unfollow(followRequest.getFollowingId());
        return new ResponseEntity(HttpStatus.OK);
    }

}
