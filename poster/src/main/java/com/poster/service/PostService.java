package com.poster.service;

import com.poster.dto.PostDto;
import com.poster.dto.UserShortInfo;
import com.poster.exception.PostNotFoundException;
import com.poster.exception.UserActionRestrictedException;
import com.poster.model.Post;
import com.poster.model.User;
import com.poster.repository.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;

    public List<PostDto> getAllPosts() {
        return postRepository.findAll().stream().map(this::convertPostToDto).collect(Collectors.toList());
    }

    public List<PostDto> getPostByUserId(Long userId) {
        return postRepository.findAllByUserId(userId).stream().map(this::convertPostToDto).collect(Collectors.toList());
    }

    public List<PostDto> getPostByFollowing(Long userId) {
        List<Long> following = userService.getFollowedUsers(userId).stream().map(UserShortInfo::getId).collect(Collectors.toList());
        return postRepository.findPostsByFollowing(following).stream().map(this::convertPostToDto).collect(Collectors.toList());
    }

    public PostDto createPost(Post post) {
        post.setUser(userService.getAuthorizedUser());
        post.setDate(LocalDateTime.now());
        return this.convertPostToDto(postRepository.save(post));
    }

    public void deletePost(Long postId) {
        Optional<Post> postOptional = postRepository.findById(postId);
        if(postOptional.isPresent()){
            Post post = postOptional.get();
            if(post.getUser().getId().equals(userService.getAuthorizedUser().getId())) {
                postRepository.deleteById(postId);
            }
            else {
                throw new UserActionRestrictedException("Access to the requested action is restricted.");
            }
        }
        else {
            throw new PostNotFoundException("The post with ID " + postId + " does not exist");
        }
    }

    public PostDto updatePost(Long postId, Post updatedPost) {
        Optional<Post> postOptional = postRepository.findById(postId);
        if(postOptional.isPresent()){
            Post post = postOptional.get();
            if(post.getUser().getId().equals(userService.getAuthorizedUser().getId())) {
                post.setText(updatedPost.getText());
                return this.convertPostToDto(postRepository.save(post));
            }
            else {
                throw new UserActionRestrictedException("Access to the requested action is restricted.");
            }
        }
        else {
            throw new PostNotFoundException("The post with ID " + postId + " does not exist");
        }
    }

    public PostDto getPostById(Long postId) {
        Optional<Post> postOptional = postRepository.findById(postId);
        if(postOptional.isPresent()) {
            return this.convertPostToDto(postOptional.get());
        }
        else throw new PostNotFoundException("The post with ID " + postId + " does not exist");
    }

    public void like(Long postId) {
        Optional<Post> postOptional = postRepository.findById(postId);
        if(postOptional.isPresent()) {
            User user = userService.getAuthorizedUser();
            Post post = postOptional.get();
            if(post.getLikedBy().contains(user)) {
                post.getLikedBy().remove(user);
            }
            else {
                post.getLikedBy().add(user);
            }
            postRepository.save(post);
        }
        else throw new PostNotFoundException("The post with ID " + postId + " does not exist");
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
