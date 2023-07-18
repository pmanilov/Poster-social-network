import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {AuthService} from "./auth.service";
import {PostModel} from "../models/post.model";

@Injectable()
export class PostService {
  private postsUrl = 'http://localhost:8080/posts/'; //getUserByToken

  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) {}

  getAllPosts(): Observable<PostModel[]> {
    const url = this.postsUrl;
    return this.http.get<PostModel[]>(url, this.authService.getAuthorizationHeader());
  }

  getAllPostsById(id: number): Observable<PostModel[]> {
    const url = this.postsUrl + "user/" + id.toString();
    return this.http.get<PostModel[]>(url, this.authService.getAuthorizationHeader());
  }

  getAllPostsByFollowing(): Observable<PostModel[]> {
    const url = this.postsUrl + "following";
    return this.http.get<PostModel[]>(url, this.authService.getAuthorizationHeader());
  }

  likePostById(post_id: number){
    const url = this.postsUrl + "like/" + post_id.toString();
    return this.http.post<any>(url, JSON.stringify(""),  this.authService.getAuthorizationHeader());
  }

  getPost(post_id: number) {
    const url = this.postsUrl + post_id;
    return this.http.get<PostModel>(url, this.authService.getAuthorizationHeader());
  }

  createPost(post_text : string){
    let post : { text: string } = {
      text: post_text
    }
    const url = this.postsUrl + "create";
    return this.http.post<any>(url, JSON.stringify(post),  this.authService.getAuthorizationHeaderWithContentType());
  }

 /* isPostLiked(post_id: number){
    const url = this.postsUrl + "/"
  }*/

}
