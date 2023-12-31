import { Component } from '@angular/core';
import {PostService} from "../services/post.service";
import {PostModel} from "../models/post.model";
import {CommentModel} from "../models/comment.model";
import {CreateCommentRequestModel} from "../models/create-comment-request.model";
import {CommentService} from "../services/comment.service";
import {ImageDataModel} from "../models/image-data.model";
import {ImageService} from "../services/image.service";

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.css']
})
export class PostComponent {
  posts: PostModel[] = [];
  newComment = "";
  viewMode: string = 'all';
  sort: string = 'date'
  constructor(
    private postService: PostService,
    private imageService: ImageService,
    private commentService: CommentService
  ) {
  }
  ngOnInit() {
    this.getPosts(this.sort);
  }
  getPosts(sort: string = "date") {
    this.postService.getAllPosts(this.sort).subscribe(
      {
        next: ((posts: PostModel[]) => {
          this.posts = posts;
          for (const post of this.posts) {
            post.showComments = false;
          }
          this.setUserPhotos();
        }),

        error: (error => {
          console.error('Error occurred while fetching posts:', error);
        })
      }
    );
  }

  getImageUrl(image : ImageDataModel): string {
    if (image) {
      return 'data:' + image.contentType + ';base64,' + image.imageData;
    }
    return '';
  }

  setUserPhotos() {
    console.error("SUka ")

    for (const post of this.posts) {
      console.error("Blia  " + post.id)

      if (post.user.hasPhoto) {
        this.imageService.getUserImage(post.user.id).subscribe(
          {
            next: ((image: ImageDataModel) => {
              console.error("SUka " + post.user.id)
              post.user.image = image;
            })
          }
        )
      }
    }
  }

  getPostsByFollowing(sort: string = "date") {
    this.postService.getAllPostsByFollowing(this.sort).subscribe(
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
          this.setCommentsPhotos(post);
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
              this.setCommentsPhotos(post);
            }
          }
        }),

        error: (error => {
          console.error('Error occurred while adding comment:', error);
        })
      }
    )
  }

  setCommentsPhotos(post : PostModel) {
    for (const comment of post.comments) {
      if (comment.user.hasPhoto) {
        this.imageService.getUserImage(comment.user.id).subscribe(
          {
            next: ((image: ImageDataModel) => {
              comment.user.image = image;
            })
          }
        )
      }
    }
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
      this.getPostsByFollowing(this.sort);
    } else {
      this.getPosts(this.sort);
    }
  }
}
