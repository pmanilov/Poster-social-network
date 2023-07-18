import { Component } from '@angular/core';
import {PostService} from "../services/post.service";
import {PostModel} from "../models/post.model";
import {CommentModel} from "../models/comment.model";
import {CreateCommentRequestModel} from "../models/create-comment-request.model";
import {CommentService} from "../services/comment.service";

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.css']
})
export class PostComponent {
  posts: PostModel[] = [];
  newComment = "";
  viewMode: string = 'all';
  constructor(
    private postService: PostService,
    private commentService: CommentService
  ) {
  }
  ngOnInit() {
    this.getPosts();
  }
  getPosts() {
    this.postService.getAllPosts().subscribe(
      {
        next: ((posts: PostModel[]) => {
          this.posts = posts;
          for (const post of this.posts) {
            post.showComments = false;
          }
        }),

        error: (error => {
          console.error('Error occurred while fetching posts:', error);
        })
      }
    );
  }

  getPostsByFollowing() {
    this.postService.getAllPostsByFollowing().subscribe(
      {
        next: ((posts: PostModel[]) => {
          this.posts = posts;
          for (const post of this.posts) {
            post.showComments = false;
          }
        }),

        error: (error => {
          console.error('Error occurred while fetching posts:', error);
        })
      }
    );
  }

  likePost(post_id: number) {
    this.postService.likePostById(post_id).subscribe(
      {
        next: ((any: any) => {
          for (const post of this.posts) {
            if (post.id == post_id) {
              this.postService.getPost(post_id).subscribe(
                {
                  next: ((updatedPost: PostModel) => {
                    post.amountOfLikes = updatedPost.amountOfLikes;
                    post.likedByCurrentUser = !post.likedByCurrentUser;
                  })
                }
              )
              break;
            }
          }
        }),

        error: (error => {
          console.error('Error occurred while adding like:', error);
        })
      }
    )
  }

  showComments(post_id: number) {
    for (const post of this.posts) {
      if (post.id == post_id) {
        if (!post.showComments) {
          this.setPostComments(post_id);
        } else {
          post.showComments = false;
        }
      } else {
        post.showComments = false;
      }
    }
  }

  private setPostComments(post_id: number) {
    this.commentService.getCommentsByPostId(post_id).subscribe(
      {
        next: ((comments: CommentModel[]) => {
          for (const post of this.posts) {
            if (post_id == post.id) {
              post.comments = comments;
              post.showComments = true;
            }
          }
        }),

        error: (error => {
          console.error('Error occurred while adding comment:', error);
        })
      }
    )
  }

  addComment(post_id: number) {
    let comment: CreateCommentRequestModel = {
      postId: post_id,
      text: this.newComment
    }
    let newComment: CommentModel | null;
    this.commentService.addCommentToPost(comment).subscribe(
      {
        next: ((comment: CommentModel) => {
          newComment = comment;
          for (const post of this.posts) {
            const postsArray: CommentModel[] = [];
            if (post.id == post_id) {
              for (const commentModel of post.comments) {
                postsArray.push(commentModel);
              }
              postsArray.push(newComment)
              post.comments = postsArray;

              this.newComment = "";
              post.amountOfComments += 1;
            }
          }
        }),

        error: (error => {
          console.error('Error occurred while adding comment:', error);
        })
      }
    )

  }

  changeView() {
    if (this.viewMode === 'following') {
      this.getPostsByFollowing();
    } else {
      this.getPosts();
    }
  }
}
