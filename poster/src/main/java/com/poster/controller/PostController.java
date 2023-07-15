package com.poster.controller;

import com.poster.dto.PostDto;
import com.poster.model.Post;
import com.poster.service.PostService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = {"Authorization"})
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getPostDtoById(postId));
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostDto>> getPostByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(postService.getPostByUserId(userId));
    }

    @PostMapping("/create")
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody Post post) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.createPost(post));
    }

    @PutMapping("/edit/{postId}")
    public ResponseEntity<PostDto> updatePost(@PathVariable Long postId, @Valid  @RequestBody Post post) {
        return ResponseEntity.ok(postService.updatePost(postId, post));
    }

    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/like/{postId}")
    public ResponseEntity<Void> likePost(@PathVariable Long postId) {
        postService.like(postId);
        return ResponseEntity.ok().build();
    }
}