import {Component} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {CookieService} from "ngx-cookie-service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  url = 'http://localhost:8080/auth/login';
  headers = new HttpHeaders().set('Content-Type', 'application/json');
  options = { headers: this.headers };

  JwtRequest = {
    email: "",
    password: ""
  };

  loginFailed = false;

  constructor(
    private http: HttpClient,
    private cookieService: CookieService,
    private router: Router
  ) {}


  login(): void {
    const json = JSON.stringify(this.JwtRequest)
    this.http.post(this.url, json, this.options).subscribe(
      {
        next: ((response: any) => {
          // add jwt token to cookie in period of 24 hours
          this.cookieService.delete('token');
          this.cookieService.set('token', response.jwtToken, 24);
          this.openProfilePage();
        }),

        error: (error => {
          console.log(error);
          this.loginFailed = true;
        })
      }
    );
  }


  openProfilePage(){
    this.router.navigate(["/profile"]);
  }

}
