import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent} from "./login/login.component";
import {RegistrationComponent} from "./registration/registration.component";
import {UserComponent} from "./user/user.component";
import {MainLayoutComponent} from "./main-layout/main-layout.component";
import {PostComponent} from "./post/post.component";
import {FollowersAndFollowingComponent} from "./followers-and-following/followers-and-following.component";

const routes: Routes = [
  { path: 'login', component: LoginComponent},
  { path: 'registration', component: RegistrationComponent},
  { path: '', component: MainLayoutComponent,
  children:[
    {path: 'profile', component: UserComponent},
    {path: 'profile/:id', component: UserComponent},
    {path: 'posts', component: PostComponent},
    {path: 'subscriptions', component: FollowersAndFollowingComponent}
  ]}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
