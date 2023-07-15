import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {AuthService} from "./auth.service";
import {PostModel} from "../models/post.model";

@Injectable()
export class PostService {
  private postsByUserIdUrl = 'http://localhost:8080/posts/user/'; //getUserByToken

  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) {}

  getAllPostsById(id: number): Observable<PostModel[]> {
    const url = this.postsByUserIdUrl + id.toString();
    return this.http.get<PostModel[]>(url, this.authService.getAuthorizationHeader());
  }

  getPost() {
    //return this.http.get<User>(this.apiUrl, this.authService.getAuthorizationHeader());
  }

}
