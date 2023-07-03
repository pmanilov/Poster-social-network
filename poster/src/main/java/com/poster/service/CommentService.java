package com.poster.service;

import com.poster.exception.CommentNotFoundException;
import com.poster.model.Comment;
import com.poster.repository.CommentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public List<Comment> getCommentByPostId(Long postId) {
        return commentRepository.findAllByPostId(postId);
    }

    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public void deleteComment(Long commentId){
        commentRepository.deleteById(commentId);
    }
    public Comment getCommentById(Long commentId) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        if(commentOptional.isPresent()){
            return commentOptional.get();
        }
        else throw new CommentNotFoundException("The comment with ID " + commentId + " does not exist");
    }

    public Comment updateComment(Long commentId, String text) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        if(commentOptional.isPresent()){
            Comment comment = commentOptional.get();
            comment.setText(text);
            return commentRepository.save(comment);
        }
        else throw new CommentNotFoundException("The comment with ID " + commentId + " does not exist");
    }
}
