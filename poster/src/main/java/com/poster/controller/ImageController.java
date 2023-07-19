package com.poster.controller;


import com.poster.dto.ImageDto;
import com.poster.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = {"Authorization"})
public class ImageController {

    private final UserService userService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<ImageDto> getUserImage(@PathVariable Long userId){
        /*return ResponseEntity.ok(imageService.getUserImage(userId));*/
        var image = userService.getUserImage(userId);
        return ResponseEntity.ok(image);
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<?> setUserImage(@PathVariable Long userId, @RequestParam("image") MultipartFile file){
        userService.uploadUserImage(file, userId);
        return ResponseEntity.ok("");
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<?> deleteUserImage(@PathVariable Long userId){
        userService.deleteUserImage(userId);
        return ResponseEntity.ok("");
    }
}
