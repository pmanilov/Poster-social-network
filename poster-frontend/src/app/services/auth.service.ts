import { Injectable } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
import {CookieService} from "ngx-cookie-service";
import {Router} from "@angular/router";

@Injectable()
export class AuthService {
  // Метод для создания заголовка авторизации

  constructor(
    private cookieService: CookieService,
    private router: Router
  ) {
  }

  getAuthorizationHeader() {
    const token = this.cookieService.get('token');
    if (!token){
      this.router.navigate(["/login"]);
    }

    // add token to header
    const requestHeader = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return { headers: requestHeader }

  }

  getAuthorizationHeaderWithContentType(){
    const token = this.cookieService.get('token');
    if (!token){
      this.router.navigate(["/login"]);
    }

    // add token to header
    const requestHeader = new HttpHeaders()
      .set('Authorization', `Bearer ${token}`)
      .set('Content-Type', 'application/json');
    return { headers: requestHeader }
  }
}
