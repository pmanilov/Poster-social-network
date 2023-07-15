import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {AuthService} from "./auth.service";
import {UserModel} from "../models/user.model";

@Injectable()
export class UserService {
  private apiUrl = 'http://localhost:8080/user/token'; //getUserByToken

  constructor(
    private http: HttpClient,
    private authService: AuthService
    ) {}

  getUser(): Observable<UserModel> {
    return this.http.get<UserModel>(this.apiUrl, this.authService.getAuthorizationHeader());
  }
}
