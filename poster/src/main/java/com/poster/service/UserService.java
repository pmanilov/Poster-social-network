package com.poster.service;

import com.poster.dto.ImageDto;
import com.poster.dto.UserDto;
import com.poster.dto.UserShortInfo;
import com.poster.exception.ImageNotFoundException;
import com.poster.exception.UserNotFoundException;
import com.poster.model.User;
import com.poster.model.UserImage;
import com.poster.repository.UserImageRepository;
import com.poster.repository.UserRepository;
import com.poster.secutiry.model.SecurityUser;
import com.poster.util.ImageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserImageRepository userImageRepository;

    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public UserDto getUserByToken() {
        return convertUserToDto(getAuthorizedUser());
    }

    public UserDto getUserById(Long id) {
        return userRepository.findById(id).map(this::convertUserToDto).orElseThrow(() -> new RuntimeException("User no found!"));
    }

    public User getUserEntityById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User no found!"));
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

    public void followUnfollow(Long targetUserId) {
        User user = getAuthorizedUser();
        log.info("UserService -> followUnfollow: A user with id = {} attempts to follow/unfollow to a user with id = {}", user.getId(), targetUserId);
        if (user.getId().equals(targetUserId)){
            log.error("UserService -> followUnfollow: Id's are equals ");
            throw new RuntimeException("Id's are equals. You can't subscribe to yourself!");
        }
        User targetUser = userRepository.findById(targetUserId).orElseThrow(() -> new RuntimeException("Can't find user to subscribe"));

        if (userRepository.isUserSubscribed(user.getId(), targetUserId)) {
            log.info("UserService -> followUnfollow: User with id = {} successfully unfollowed from user with id = {}", user.getId(), targetUserId);
            userRepository.unSubscribe(user.getId(), targetUserId);
        } else {
            log.info("UserService -> followUnfollow: User with id = {} successfully followed to user with id = {}", user.getId(), targetUserId);

            userRepository.addSubscription(user.getId(), targetUser.getId());
        }
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
                .hasPhoto(userImageRepository.isUserHasPhoto(user.getId()))
                .build();
    }

    private UserShortInfo convertUserToShortInfo(User user) {
        return UserShortInfo.builder()
                .id(user.getId())
                .username(user.getUsername())
                .hasPhoto(userImageRepository.isUserHasPhoto(user.getId()))
                .build();
    }

    public boolean checkOwnership(long userId){
        return getAuthorizedUser().getId().equals(userId);
    }

    public boolean isSubscribed(Long id) {
        return userRepository.isUserSubscribed(getAuthorizedUser().getId(), id);
    }

    @Transactional
    public void uploadUserImage(MultipartFile file, Long userId){
        User user;
        try {
            user = getUserEntityById(userId);
        } catch (Exception e) {
            log.error("ImageService -> uploadUserImage: User with id = {} not found!", userId);
            throw new UserNotFoundException(e.getMessage());
        }
        if (userImageRepository.findByUserId(user.getId()).isPresent()) {
            userImageRepository.deleteUserImageByUserId(user.getId());
        }
        try {
            userImageRepository.save(convertFileToImage(file, user));
        } catch (IOException e){
            log.error("Error happened while converting file");
        }
    }

    @Transactional
    public ImageDto getUserImage(Long userId) {
        try{
            getUserEntityById(userId);
        } catch (UserNotFoundException e){
            throw new UserNotFoundException(e.getMessage());
        }
        return userImageRepository.findByUserId(userId).map(this::convertUserImageToDto)
                .orElseThrow(() -> new ImageNotFoundException("Image not found"));
    }

    public ImageDto convertUserImageToDto(UserImage userImage){
        return ImageDto.builder()
                .contentType(userImage.getContentType())
                .originalFileName(userImage.getOriginalFileName())
                .imageData(ImageUtils.decompressImage(userImage.getImageData()))
                .build();
    }

    /*//TODO: rename method
    @Transactional
    public boolean userHasPhoto(Long userId){
        return userImageRepository.findByUserId(userId).isPresent();
    }*/
    public boolean hasPhoto(Long userId){
        return userImageRepository.isUserHasPhoto(userId);
    }

    private UserImage convertFileToImage(MultipartFile file, User user) throws IOException {
        return UserImage.builder()
                .originalFileName(file.getOriginalFilename())
                .contentType(file.getContentType())
                .imageData(ImageUtils.compressImage(file.getBytes()))
                .user(user)
                .build();
    }
  
    public List<UserShortInfo> searchUsers(String partOfName) {
        List<User> users = userRepository.findAll();
        List<UserShortInfo> resultOfSearch = new ArrayList<>();
        for(User user : users){
            if(user.getUsername().startsWith(partOfName) && !user.getId().equals(this.getAuthorizedUser().getId())){
                resultOfSearch.add(this.convertUserToShortInfo(user));
            }
        }
        return resultOfSearch;
    }
}
