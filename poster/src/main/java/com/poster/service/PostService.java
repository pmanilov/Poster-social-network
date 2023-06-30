package com.poster.service;

import com.poster.exception.PostNotFoundException;
import com.poster.model.Post;
import com.poster.repository.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }
    public List<Post> getPostByUserId(Long userId) {
        return postRepository.findAllByUserId(userId);
    }
    public Post createPost(Post post) {
        return postRepository.save(post);
    }
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
    public Post updatePost(Long id, String text) {
        Optional<Post> postOptional = postRepository.findById(id);
        if(postOptional.isPresent()){
            Post post = postOptional.get();
            post.setText(text);
            return postRepository.save(post);
        }
        else {
            throw new PostNotFoundException("The post with ID " + id + " does not exist");
        }
    }

    public Post getPostById(Long id) {
        Optional<Post> postOptional = postRepository.findById(id);
        if(postOptional.isPresent()) {
            return postOptional.get();
        }
        else throw new PostNotFoundException("The post with ID " + id + " does not exist");
    }
}
