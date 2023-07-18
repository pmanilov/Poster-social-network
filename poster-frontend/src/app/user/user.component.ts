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
import {ActivatedRoute, Router} from "@angular/router";
import {Observable, Subscription} from "rxjs";

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {
  user!: UserModel;
  posts: PostModel[] = [];
  newComment = "";
  ownerPage = true;
  isSubscribed = false;
  showCreatePost = false;
  postText = "";
  selectedFile: File | null = null;


  constructor(
    private router: Router,
    private http: HttpClient,
    private route: ActivatedRoute,
    private cookieService: CookieService,
    private userService: UserService,
    private postService: PostService,
    private commentService: CommentService
  ) {
  }

  ngOnInit() {
    this.getUser();
    this.getUserIdFromUrl()
  }

  getUser() {
    const id = this.getUserIdFromUrl();
    if (id == null) {
      this.ownerPage = true;
      this.getUserByToken();
    } else {
      this.userService.checkOwnership(id).subscribe(
        {
          next: ((isOwner: boolean) => {
            this.ownerPage = isOwner;
            this.getUserById(id);
          }),
          error: (error => {
            console.error('Error occurred while trying to ckeck ownership:', error);

          })
        }
      )
    }
  }

  getUserByToken() {
    this.userService.getUserByToken().subscribe(
      {
        next: ((user: UserModel) => {
          this.user = user;
          this.getPosts();
        }),

        error: (error => {
          console.error('Error occurred while fetching user:', error);
        })
      }
    )
  }

  getUserById(user_id: string) {
    this.userService.getUserById(user_id).subscribe(
      {
        next: ((user: UserModel) => {
          this.user = user;
          this.getPosts();
          this.isUserSubscribed(user_id)
        }),

        error: (error => {
          console.error('Error occurred while fetching user:', error);
          this.router.navigate(["/profile"]);
        })
      }
    )
  }

  isUserSubscribed(user_id: string){
    this.userService.isSubscribed(user_id).subscribe(
      {
        next: ((subscribed: boolean) => {
          this.isSubscribed = subscribed;
        }),

        error: (error => {
          console.error('Error occurred while checking subscription on user:', error);
        })
      }
    )
  }

  subscribeOnUser(){
    this.userService.subscribeOnUser(this.user.id).subscribe(
      {
        next: (() => {
          if (this.isSubscribed){
            this.isSubscribed = false;
            this.user.amountOfFollowers -= 1;
          } else {
            this.isSubscribed = true;
            this.user.amountOfFollowers += 1;
          }
        }),

        error: (error => {
          console.error('Error occurred while subscribing on user:', error);
        })
      }
    )
  }

  getUserIdFromUrl(): string | null {
    return this.route.snapshot.paramMap.get('id');
  }

  getPosts() {
    this.postService.getAllPostsById(this.user.id).subscribe(
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

  async updateAmountOfLikes(post_id: number) {
    for (const post of this.posts) {
      if (post.id = post_id) {
        const fetchedPost = await this.getPost(post_id);
        post.amountOfLikes = fetchedPost.amountOfLikes;
      }
    }
  }

  async updateAmountOfComments(post_id: number) {
    for (const post of this.posts) {
      if (post.id = post_id) {
        const fetchedPost = await this.getPost(post_id);
        post.amountOfComments = fetchedPost.amountOfComments;
      }
    }
  }

  getPost(post_id: number): Promise<PostModel> {
    let post: PostModel;
    return new Promise((resolve, reject) => {
      this.postService.getPost(post_id).subscribe({
        next: (fetchedPost: PostModel) => {
          post = fetchedPost;
          resolve(post);
        },
        error: (error) => {
          console.error('Error occurred while fetching post:', error);
          reject(error);
        }
      });
    });
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

  toggleCreatePost() {
    this.showCreatePost = !this.showCreatePost;
  }

  submitPost() {
    this.postService.createPost(this.postText).subscribe(
      {
        next: ((post: PostModel) => {
          this.addPostAndUpdateAll(post);
          this.closeCreatePost();
        }),

        error: (error => {
          console.error('Error occurred while fetching user:', error);
        })
      }
    )

  }

  addPostAndUpdateAll(newPost : PostModel){
    const updatedPosts: PostModel[] = [];
    updatedPosts.push(newPost);
    for (const post of this.posts) {
      updatedPosts.push(post);
    }
    this.posts = updatedPosts;

  }

  handleFileInput($event: Event) {
    /*this.selectedFile = event.target.files[0];*/
  }

  closeCreatePost() {
    this.selectedFile = null;
    this.postText = "";
    this.showCreatePost = false;
  }

  writeMessage() {

  }
}
