package com.poster.controller;

import com.poster.dto.UserDto;
import com.poster.dto.UserShortInfo;
import com.poster.model.FollowRequest;
import com.poster.model.User;
import com.poster.secutiry.JwtHelper;
import com.poster.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtHelper jwtHelper;
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id){
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }


    @GetMapping()
    public ResponseEntity<List<UserDto>> getAllUsers(){
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/followers/{userId}")
    public ResponseEntity<List<UserShortInfo>> getUsersFollowers(@PathVariable Long userId){
        return new ResponseEntity<>(userService.getUserFollowers(userId), HttpStatus.OK);    }

    @GetMapping("/following/{userId}")
    public ResponseEntity<List<UserShortInfo>> getUserFollowing(@PathVariable Long userId){
        return new ResponseEntity<>(userService.getUserFollowing(userId), HttpStatus.OK);
    }

    @PostMapping("/follow")
    public ResponseEntity follow(@RequestBody FollowRequest followRequest){
        //TODO: change param name

        return new ResponseEntity(HttpStatus.OK);
    }

    //creare User is in auth controller right now
    //update user info

    //what to retrurn to page; maybe we will return object UserPageInfo;

    //forgot password

    //
}
