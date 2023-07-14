import {Component, OnInit} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {CookieService} from "ngx-cookie-service";

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {
  user: any;
  posts: any[] = [];
  getUserUrl = 'http://localhost:8080/user/token';
  username = "";
  amountOfFollowers = 100;
  amountOfFollowing = 100;
  about = "";


  constructor(
    private http: HttpClient,
    private cookieService: CookieService
    ) {}

  ngOnInit() {
    this.getUser();
  }

  getUser() {
    let token = this.getJwtToken();
    let requestHeader = new HttpHeaders().set("Authorization", "Bearer " + token);
    let options = { headers: requestHeader }

    this.http.get(this.getUserUrl, options).subscribe(
      {
        next: ((response: any) => {
          this.username = response.username;
          this.amountOfFollowers = response.amountOfFollowers;
          this.amountOfFollowing = response.amountOfFollowing;
          this.about = response.about;
        }),

        error: (error => {
          console.error('Error occurred while fetching user:', error);
        })
      }
    );
  }

  /*getPosts() {
    this.http.get<any[]>('api/posts').subscribe(
      {
        next: ((response: any) => {
          this.posts = response;
        }),

        error: (error => {
          console.error('Error occurred while fetching posts:', error);
        })
      }
    );
  }*/

  toggleComments(post: any){
    post.showComments = !post.showComments;
  }

  getJwtToken(){
    return this.cookieService.get('token');
  }
}
