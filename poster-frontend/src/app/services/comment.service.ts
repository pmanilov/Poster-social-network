import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {AuthService} from "./auth.service";
import {PostModel} from "../models/post.model";
import {CommentModel} from "../models/comment.model";
import {CreateCommentRequestModel} from "../models/create-comment-request.model";

@Injectable()
export class CommentService {
  private commentsUrl = 'http://localhost:8080/comments/'


  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) {}


  getCommentsByPostId(post_id: number): Observable<CommentModel[]>{
    const url = this.commentsUrl + "post/" + post_id.toString();
    return this.http.get<CommentModel[]>(url, this.authService.getAuthorizationHeader());
  }

  addCommentToPost(comment: CreateCommentRequestModel){
    const url = this.commentsUrl + "create";
    return this.http.post<CommentModel>(url, JSON.stringify(comment), this.authService.getAuthorizationHeaderWithContentType());
  }
}
