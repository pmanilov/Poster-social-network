import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {CookieService} from "ngx-cookie-service";
import {UserModel} from "../models/user.model";
import {UserService} from "../services/user.service";
import {PostService} from "../services/post.service";
import {PostModel} from "../models/post.model";
import {CommentModel} from "../models/comment.model";
import {CommentService} from "../services/comment.service";
import {CreateCommentRequestModel} from "../models/create-comment-request.model";

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {
  user!: UserModel;
  posts: PostModel[] = [];
  newComment = "";


  constructor(
    private http: HttpClient,
    private cookieService: CookieService,
    private userService: UserService,
    private postService: PostService,
    private commentService: CommentService
    ) {}

  ngOnInit() {
    this.getUser();
    this.getPosts();
  }

  getUser() {
    this.userService.getUser().subscribe(
      {
        next: ((user: UserModel) => {
            this.user = user;
        }),

        error: (error => {
          console.error('Error occurred while fetching user:', error);
        })
      }
    );
  }

  getPosts() {
    this.postService.getAllPostsById(1).subscribe(
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

  likePost(post_id: number){

  }

  showComments(post_id: number){
    for (const post of this.posts) {
      if (post.id == post_id){
        if (!post.showComments){
          this.setPostComments(post_id);
        } else {
          post.showComments = false;
        }
      } else {
        post.showComments = false;
      }
    }
  }

  private setPostComments(post_id: number){
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

  addComment(post_id: number){
    let comment: CreateCommentRequestModel = {
      postId: post_id,
      text: this.newComment
    }
    let newComment : CommentModel | null;
    this.commentService.addCommentToPost(comment).subscribe(
      {
        next: ((comment: CommentModel) => {
          newComment = comment;
          for (const post of this.posts) {
            const postsArray: CommentModel[] = [];
            if (post.id == post_id){
              for (const commentModel of post.comments) {
                postsArray.push(commentModel);
              }
              postsArray.push(newComment)
              post.comments = postsArray;

              this.newComment = "";
            }
          }
        }),

        error: (error => {
          console.error('Error occurred while adding comment:', error);
        })
      }
    )

  }

}
