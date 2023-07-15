package com.poster.service;

import com.poster.dto.CommentDto;
import com.poster.dto.UserShortInfo;
import com.poster.exception.CommentNotFoundException;
import com.poster.exception.UserActionRestrictedException;
import com.poster.model.Comment;
import com.poster.model.Post;
import com.poster.model.User;
import com.poster.repository.CommentRepository;
import com.poster.request.CreateCommentRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final PostService postService;


    public List<CommentDto> getCommentByPostId(Long postId) {
        return commentRepository.findAllByPostId(postId).stream().map(this::convertCommentToDto).collect(Collectors.toList());
    }

    public CommentDto createComment(CreateCommentRequest commentRequest) {
        Comment comment = Comment.builder()
                .text(commentRequest.getText())
                .user(userService.getAuthorizedUser())
                .post(postService.getPostById(commentRequest.getPostId())).build();
        comment.setDate(LocalDateTime.now());
        return this.convertCommentToDto(commentRepository.save(comment));
    }

    public void deleteComment(Long commentId) {
        Optional<Comment> postOptional = commentRepository.findById(commentId);
        if (postOptional.isPresent()) {
            Comment comment = postOptional.get();
            if (comment.getUser().getId().equals(userService.getAuthorizedUser().getId())) {
                commentRepository.deleteById(commentId);
            } else {
                throw new UserActionRestrictedException("Access to the requested action is restricted.");
            }
        } else {
            throw new CommentNotFoundException("The comment with ID " + commentId + " does not exist");
        }
    }

    public CommentDto getCommentById(Long commentId) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        if (commentOptional.isPresent()) {
            return this.convertCommentToDto(commentOptional.get());
        } else throw new CommentNotFoundException("The comment with ID " + commentId + " does not exist");
    }

    public CommentDto updateComment(Long commentId, Comment updatedComment) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        if (commentOptional.isPresent()) {
            Comment comment = commentOptional.get();
            if (comment.getUser().getId().equals(userService.getAuthorizedUser().getId())) {
                comment.setText(updatedComment.getText());
                return this.convertCommentToDto(commentRepository.save(comment));
            } else {
                throw new UserActionRestrictedException("Access to the requested action is restricted.");
            }
        } else throw new CommentNotFoundException("The comment with ID " + commentId + " does not exist");
    }

    private CommentDto convertCommentToDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .date(comment.getDate())
                .postId(comment.getPost().getId())
                .user(this.convertUserToShortInfo(comment.getUser()))
                .build();
    }

    private UserShortInfo convertUserToShortInfo(User user) {
        return UserShortInfo.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();
    }
}
