import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {AuthService} from "./auth.service";
import {UserModel} from "../models/user.model";
import {UserShortInfoModel} from "../models/user-short-info.model";

@Injectable()
export class UserService {
  private apiUrl = 'http://localhost:8080/user/'; //getUserByToken

  constructor(
    private http: HttpClient,
    private authService: AuthService
    ) {}

  getUserByToken(): Observable<UserModel> {
    const url = this.apiUrl + "token";
    return this.http.get<UserModel>(url, this.authService.getAuthorizationHeader());
  }

  checkOwnership(user_id : string){
    const url = this.apiUrl + "ownership/" + user_id;
    return this.http.get<boolean>(url, this.authService.getAuthorizationHeader());
  }

  getUserById(user_id : string): Observable<UserModel>{
    const url = this.apiUrl + user_id;
    return this.http.get<UserModel>(url, this.authService.getAuthorizationHeader());
  }

  getFollowersById(user_id: number){
    const url = this.apiUrl + "followers/" + user_id.toString();
    return this.http.get<UserShortInfoModel[]>(url, this.authService.getAuthorizationHeader());
  }

  getFollowingById(user_id: number){
    const url = this.apiUrl + "followed/" + user_id.toString();
    return this.http.get<UserShortInfoModel[]>(url, this.authService.getAuthorizationHeader());
  }

  isSubscribed(user_id: string){
    const url = this.apiUrl + "subscribed/" + user_id;
    return this.http.get<boolean>(url, this.authService.getAuthorizationHeader());
  }

  subscribeOnUser(user_id : number){
    let subscribeResponse : { followingId: number } = {
      followingId: user_id
    }
    const url = this.apiUrl + "subscribe";
    return this.http.post<any>(url, JSON.stringify(subscribeResponse),  this.authService.getAuthorizationHeaderWithContentType());
  }

  searchUsers(query : string){
    const url = this.apiUrl + "search?query="+query;
    return this.http.get<UserShortInfoModel[]>(url, this.authService.getAuthorizationHeader());
  }
}
