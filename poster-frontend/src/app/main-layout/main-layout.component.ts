import { Component, OnInit } from '@angular/core';
import {CookieService} from "ngx-cookie-service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-main-layout',
  templateUrl: './main-layout.component.html',
  styleUrls: ['./main-layout.component.css']
})
export class MainLayoutComponent implements OnInit{


  constructor(
    private cookieService: CookieService,
    private router: Router
  ) {
  }

  ngOnInit(){
    const token = this.cookieService.get('token');
    if (!token){
      this.openLoginPage()
    }
    //TODO: add and hold user info for header
  }

  openLoginPage(){
    this.router.navigate(["/login"]);
  }
}
