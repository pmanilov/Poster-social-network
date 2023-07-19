package com.poster.service;

import com.poster.dto.ImageDto;
import com.poster.exception.ImageNotFoundException;
import com.poster.exception.UserNotFoundException;
import com.poster.model.User;
import com.poster.model.UserImage;
import com.poster.repository.PostImageRepository;
import com.poster.repository.UserImageRepository;
import com.poster.util.ImageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

//@Service
//@RequiredArgsConstructor
//@Slf4j
public class ImageService {

//    //private final PostImageRepository postImageRepository;
//    private final UserImageRepository userImageRepository;
//    //private final PostService postService;
//    private final UserService userService;
//
//    public void uploadUserImage(MultipartFile file, Long userId){
//        User user;
//        try {
//            user = userService.getUserEntityById(userId);
//        } catch (Exception e) {
//            log.error("ImageService -> uploadUserImage: User with id = {} not found!", userId);
//            throw new UserNotFoundException(e.getMessage());
//        }
//        if (userImageRepository.findByUserId(user.getId()).isPresent()) {
//            userImageRepository.deleteUserImageByUserId(user.getId());
//        }
//        try {
//            userImageRepository.save(convertFileToImage(file, user));
//        } catch (IOException e){
//            log.error("Error happened while converting file");
//        }
//    }
//
//    @Transactional
//    public ImageDto getUserImage(Long userId) {
//        try{
//            userService.getUserEntityById(userId);
//        } catch (UserNotFoundException e){
//            throw new UserNotFoundException(e.getMessage());
//        }
//        return userImageRepository.findByUserId(userId).map(this::convertUserImageToDto)
//                .orElseThrow(() -> new ImageNotFoundException("Image not found"));
//    }
//
//    public ImageDto convertUserImageToDto(UserImage userImage){
//        return ImageDto.builder()
//                .contentType(userImage.getContentType())
//                .originalFileName(userImage.getOriginalFileName())
//                .imageData(ImageUtils.decompressImage(userImage.getImageData()))
//                .build();
//    }
//
//    //TODO: rename method
//    public boolean userHasPhoto(Long userId){
//        return userImageRepository.findByUserId(userId).isPresent();
//    }
//
//   /* @Transactional
//    public String updateImage(MultipartFile file, Long userId) throws IOException {
//        User user = userService.findById(userId);
//        if (user == null) {
//            return null;
//        }
//        Image image = user.getImage();
//        if (image != null) {
//            imageRepository.deleteById(user.getImage().getId());
//            imageRepository.save(convertFileToImage(file, user));
//            return "Image updated successfully";
//        }
//        return null;
//    }
//
//    public Boolean deleteImage(Long userId) {
//        User user = userService.findById(userId);
//        if (user == null) {
//            return false;
//        }
//        Optional<Image> image = userImageRepository.findByUserId(userId);
//        if (image.isEmpty()) {
//            return null;
//        }
//        imageRepository.deleteById(image.get().getId());
//        return true;
//    }*/
//
//    private UserImage convertFileToImage(MultipartFile file, User user) throws IOException {
//        return UserImage.builder()
//                .originalFileName(file.getOriginalFilename())
//                .contentType(file.getContentType())
//                .imageData(ImageUtils.compressImage(file.getBytes()))
//                .user(user)
//                .build();
//    }
}
