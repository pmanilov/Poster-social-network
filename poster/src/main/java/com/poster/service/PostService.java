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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;

    public List<PostDto> getAllPosts(String sort) {
        if(sort.equals("date")) {
            return postRepository.findAll().stream().map(this::convertPostToDto).collect(Collectors.toList());
        }
        else {
            return postRepository.findAllOrderByLikesCountDesc().stream().map(this::convertPostToDto).collect(Collectors.toList());
        }
    }

    public List<PostDto> getPostByUserId(Long userId) {
        List<PostDto> postDtos = new ArrayList<>(postRepository.findAllByUserIdOrderByDateDesc(userId)
                .stream().map(this::convertPostToDto).toList());
        return postDtos;
    }

    public List<PostDto> getPostByFollowing(String sort) {
        List<Long> following = userService.getFollowedUsers(userService.getAuthorizedUser().getId()).stream().map(UserShortInfo::getId).collect(Collectors.toList());
        if(sort.equals("date")) {
            return postRepository.findPostsByFollowing(following).stream().map(this::convertPostToDto).collect(Collectors.toList());
        }
        else {
            return postRepository.findPostsByFollowingOrderByLikesCountDesc(following).stream().map(this::convertPostToDto).collect(Collectors.toList());
        }
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

    public PostDto getPostDtoById(Long postId) {
        Optional<Post> postOptional = postRepository.findById(postId);
        if(postOptional.isPresent()) {
            return this.convertPostToDto(postOptional.get());
        }
        else throw new PostNotFoundException("The post with ID " + postId + " does not exist");
    }

    public Post getPostById(Long postId) {
        return postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("The post with ID " + postId + " does not exist"));
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        return PostDto.builder()
                .id(post.getId())
                .text(post.getText())
                .date(post.getDate().format(formatter))
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

    public boolean isPostLiked(Long postId) {
        if (postRepository.findById(postId).isEmpty()){
            throw new PostNotFoundException("There is no post with id = " + postId + " !!");
        }
        return postRepository.isPostLikedByUser(postId, userService.getAuthorizedUser().getId());
    }
}
