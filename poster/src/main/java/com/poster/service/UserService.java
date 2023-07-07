package com.poster.service;

import com.poster.dto.UserDto;
import com.poster.dto.UserShortInfo;
import com.poster.model.User;
import com.poster.repository.UserRepository;
import com.poster.secutiry.model.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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

    public List<UserShortInfo> getFollowedUsers(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException(("User not found!")))
                .getFollowing().stream().map(this::convertUserToShortInfo).collect(Collectors.toList());
    }

    public void follow(Long targetUserId) {
        User user = getAuthorizedUser();
        if (user.getId().equals(targetUserId)){
            throw new RuntimeException("Id's are equals. You can't subscribe to yourself!");
        }
        User targetUser = userRepository.findById(targetUserId).orElseThrow(() -> new RuntimeException("Can't find user to subscribe"));

        if (userRepository.isUserSubscribed(user.getId(), targetUserId)) {
            throw new RuntimeException("You are already subscribed");
        }
        userRepository.addSubscription(user.getId(), targetUser.getId());

    }

    public void unfollow(Long targetUserId) {
        User user = getAuthorizedUser();
        userRepository.unSubscribe(user.getId(), targetUserId);
    }

    public User getAuthorizedUser() {
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByEmail(securityUser.getUsername())
                .orElseThrow(() -> new RuntimeException("Wrong authorization info"));
    }

    private UserDto convertUserToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .about(user.getAbout())
                .amountOfFollowers(user.getFollowers().size())
                .amountOfFollowing(user.getFollowing().size())
                .amountOfPosts(user.getPosts().size())
                .build();
    }

    private UserShortInfo convertUserToShortInfo(User user) {
        return UserShortInfo.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();
    }
}
