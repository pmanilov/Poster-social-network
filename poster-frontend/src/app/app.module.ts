import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { AppRoutingModule } from "./app-routing.module";
import { RegistrationComponent } from './registration/registration.component';
import { UserComponent } from './user/user.component';
import { PostComponent } from './post/post.component';
import { FormsModule } from "@angular/forms";
import { HttpClientModule } from "@angular/common/http";
import { MainLayoutComponent } from './main-layout/main-layout.component';
import { UserService } from "./services/user.service";
import { AuthService } from "./services/auth.service";
import { PostService } from "./services/post.service";
import { CommentService } from "./services/comment.service";
import { FollowersAndFollowingComponent } from './followers-and-following/followers-and-following.component';
import { ChatComponent } from './chat/chat.component';
import { ChatService } from "./services/chat.service";
import {ImageService} from "./services/image.service";
import { SearchComponent } from './search/search.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegistrationComponent,
    UserComponent,
    PostComponent,
    MainLayoutComponent,
    FollowersAndFollowingComponent,
    ChatComponent,
    SearchComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [
    UserService,
    PostService,
    AuthService,
    CommentService,
    ChatService,
    ImageService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
