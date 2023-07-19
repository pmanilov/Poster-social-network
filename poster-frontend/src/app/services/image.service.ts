import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {AuthService} from "./auth.service";
import {ImageDataModel} from "../models/image-data.model";

@Injectable()
export class ImageService {
  private imageUrl = 'http://localhost:8080/image/'; //getUserByToken



  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) {}

  getUserImage(user_id : number){
    const url = this.imageUrl + "user/" + user_id.toString();
    return this.http.get<ImageDataModel>(url, this.authService.getAuthorizationHeader());
  }

  uploadUserImage(formData : FormData, user_id : number){
    const url = this.imageUrl + "user/" + user_id.toString();

    return this.http.post(url, formData, this.authService.getAuthorizationHeader());
  }

  deleteUserPhoto(user_id : number){
    const url = this.imageUrl + "user/" + user_id.toString();
    return this.http.delete(url, this.authService.getAuthorizationHeader());

  }




}
