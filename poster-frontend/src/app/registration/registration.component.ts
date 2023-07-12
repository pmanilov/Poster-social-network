import { Component } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Router} from "@angular/router";

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent {
  /*username:string = "";
  email:string = "";
  password:string = "";
  about:string = "";
  submitted = false;*/
  isFormInvalid = false;
  registrationFailed = false;
  url = 'http://localhost:8080/auth/create-user';

  headers = new HttpHeaders().set('Content-Type', 'application/json');
  options = { headers: this.headers };

  registrationData = {
    username: "",
    email: "",
    password: "",
    about: ""
  }

  constructor(
    private http: HttpClient,
    private router: Router
  ) {
  }

  register(){
    this.isFormInvalid = false;
    if (this.isAnyFieldEmpty()) {
      this.isFormInvalid = true;
      return;
    }

    const jsonData = JSON.stringify(this.registrationData);

    this.http.post(this.url, jsonData, this.options).subscribe(
      {
        next: ((response: any) => {
          this.openLoginPage()
        }),

        error: (error => {
          console.log(error);
          this.registrationFailed = true;
        })
      }
    );
  }

  openLoginPage(){
    this.router.navigate(["/login"]);
  }

  isFieldEmpty(value: string): boolean {
    return !value || value.trim().length === 0;
  }

  isAnyFieldEmpty(): boolean {
    return this.isFieldEmpty(this.registrationData.username) || this.isFieldEmpty(this.registrationData.email) || this.isFieldEmpty(this.registrationData.password);
  }
}
