package com.poster.service;

import com.poster.dto.CommentDto;
import com.poster.dto.UserShortInfo;
import com.poster.exception.CommentNotFoundException;
import com.poster.model.Comment;
import com.poster.model.User;
import com.poster.repository.CommentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public List<CommentDto> getCommentByPostId(Long postId) {
        return commentRepository.findAllByPostId(postId).stream().map(this::convertCommentToDto).collect(Collectors.toList());
    }

    public CommentDto createComment(Comment comment) {
        return this.convertCommentToDto(commentRepository.save(comment));
    }

    public void deleteComment(Long commentId){
        commentRepository.deleteById(commentId);
    }
    public CommentDto getCommentById(Long commentId) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        if(commentOptional.isPresent()){
            return this.convertCommentToDto(commentOptional.get());
        }
        else throw new CommentNotFoundException("The comment with ID " + commentId + " does not exist");
    }

    public CommentDto updateComment(Long commentId, String text) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        if(commentOptional.isPresent()){
            Comment comment = commentOptional.get();
            comment.setText(text);
            return this.convertCommentToDto(commentRepository.save(comment));
        }
        else throw new CommentNotFoundException("The comment with ID " + commentId + " does not exist");
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
    private UserShortInfo convertUserToShortInfo(User user){
        return UserShortInfo.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();
    }
}
