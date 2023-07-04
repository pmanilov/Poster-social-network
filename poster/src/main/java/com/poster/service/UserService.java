package com.poster.service;

import com.poster.dto.UserDto;
import com.poster.dto.UserShortInfo;
import com.poster.model.User;
import com.poster.repository.UserRepository;
import com.poster.secutiry.JwtHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mapping.callback.ReactiveEntityCallbacks;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtHelper jwtHelper;

    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public UserDto getUserById(Long id) {
        return userRepository.findById(id).map(this::convertUserToDto).orElseThrow(() -> new RuntimeException("User no found!"));
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(this::convertUserToDto).collect(Collectors.toList());
    }

    public List<UserShortInfo> getUserFollowers(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException(("User not found!")))
                .getFollowers().stream().map(this::convertUserToShortInfo).collect(Collectors.toList());
    }

    public List<UserShortInfo> getUserFollowing(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException(("User not found!")))
                .getFollowing().stream().map(this::convertUserToShortInfo).collect(Collectors.toList());
    }

    public void follow(String userToken, Long followingUserId){
       /* User followingUser = userRepository.findById(followingUserId)
                .orElseThrow(() -> new RuntimeException("Can't follow because following user is not found!!"));
        User follower = userRepository.findByEmail(jwtHelper
                .getUsernameFromToken(jwtHelper.injectTokenFromAuthorization(userToken)))
                .orElseThrow(() -> new RuntimeException("Wrong authorization info"));
        Set<User> followingUserFollowers = followingUser.getFollowers();
        followingUserFollowers.add(follower);*/

    }

    private UserDto convertUserToDto(User user){
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .about(user.getAbout())
                .amountOfFollowers(user.getFollowers().size())
                .amountOfFollowing(user.getFollowing().size())
                .amountOfPosts(user.getPosts().size())
                .build();
    }


    private UserShortInfo convertUserToShortInfo(User user){
        return UserShortInfo.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();
    }
}
