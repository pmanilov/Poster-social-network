package com.poster.service;

import com.poster.dto.PostDto;
import com.poster.dto.UserShortInfo;
import com.poster.exception.PostNotFoundException;
import com.poster.model.Post;
import com.poster.model.User;
import com.poster.repository.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public List<PostDto> getAllPosts() {
        return postRepository.findAll().stream().map(this::convertPostToDto).collect(Collectors.toList());
    }
    public List<PostDto> getPostByUserId(Long userId) {
        return postRepository.findAllByUserId(userId).stream().map(this::convertPostToDto).collect(Collectors.toList());
    }
    public PostDto createPost(Post post) {
        return this.convertPostToDto(postRepository.save(post));
    }
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
    public PostDto updatePost(Long id, String text) {
        Optional<Post> postOptional = postRepository.findById(id);
        if(postOptional.isPresent()){
            Post post = postOptional.get();
            post.setText(text);
            return this.convertPostToDto(postRepository.save(post));
        }
        else {
            throw new PostNotFoundException("The post with ID " + id + " does not exist");
        }
    }

    public PostDto getPostById(Long id) {
        Optional<Post> postOptional = postRepository.findById(id);
        if(postOptional.isPresent()) {
            return this.convertPostToDto(postOptional.get());
        }
        else throw new PostNotFoundException("The post with ID " + id + " does not exist");
    }

    private PostDto convertPostToDto(Post post){
        return PostDto.builder()
                .id(post.getId())
                .text(post.getText())
                .date(post.getDate())
                .user(this.convertUserToShortInfo(post.getUser()))
                .amountOfComments(post.getComments().size())
                .likedBy(post.getLikedBy().stream().map(this::convertUserToShortInfo).collect(Collectors.toSet()))
                .amountOfLikes(post.getLikedBy().size())
                .build();
    }

    private UserShortInfo convertUserToShortInfo(User user){
        return UserShortInfo.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();
    }
}
