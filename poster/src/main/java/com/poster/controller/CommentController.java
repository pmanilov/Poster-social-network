package com.poster.controller;

import com.poster.dto.CommentDto;
import com.poster.model.Comment;
import com.poster.request.CreateCommentRequest;
import com.poster.service.CommentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = {"Authorization", "Content-Type"})
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable Long commentId) {
        return ResponseEntity.ok(commentService.getCommentById(commentId));
    }
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentDto>> getCommentByPostId(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentByPostId(postId));
    }

    @PostMapping("/create")
    public ResponseEntity<CommentDto> createComment(@RequestBody CreateCommentRequest commentRequest) {
        System.out.println(commentRequest.getText() + " " + commentRequest.getPostId());
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.createComment(commentRequest));
    }

    @PutMapping("/edit/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable Long commentId, @Valid @RequestBody Comment comment) {
        return ResponseEntity.ok(commentService.updateComment(commentId, comment));
    }

    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
