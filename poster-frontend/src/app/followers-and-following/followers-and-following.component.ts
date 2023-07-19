import {Component, OnInit} from '@angular/core';
import {UserShortInfoModel} from "../models/user-short-info.model";
import {PostModel} from "../models/post.model";
import {UserService} from "../services/user.service";
import {UserModel} from "../models/user.model";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-followers-and-following',
  templateUrl: './followers-and-following.component.html',
  styleUrls: ['./followers-and-following.component.css']
})
export class FollowersAndFollowingComponent implements OnInit{
  followers: UserShortInfoModel[] = [];
  following: UserShortInfoModel[] = [];
  selectedTab = "following";
  user!: UserModel;
  guest!: UserModel;
  ownerPage = false;

  constructor(
    private userService: UserService,
    private route: ActivatedRoute,
    private router: Router
  ) {
  }
  newItem: string = "";
  items: string[] = [];


  ngOnInit(){
    const id = this.getUserIdFromUrl();
    if (id == null){
      this.ownerPage = true;
    } else {
      this.ownerPage = false;
      this.getUserById(id);
    }
    this.getUserByToken();

  }

  getUserById(user_id : string){
    this.userService.getUserById(user_id).subscribe(
      {
        next: ((user: UserModel) => {
          this.user = user;
          this.getFollowers();
          this.getFollowing();
        })
      }
    )
  }

  getUserByToken() {
    this.userService.getUserByToken().subscribe(
      {
        next: ((user: UserModel) => {
          this.guest = user;
          if(this.ownerPage){
            this.user = this.guest;
            this.getFollowers();
            this.getFollowing();
          }
        }),

        error: (error => {
          console.error('Error occurred while fetching user:', error);
        })
      }
    )
  }

  getFollowers(){
    this.userService.getFollowersById(this.user.id).subscribe(
      {
        next: ((followers: UserShortInfoModel[]) => {
          this.followers = followers;
        }),

        error: (error => {
          console.error('Error occurred while fetching followers:', error);
        })
      }
    );
  }

  getFollowing(){
    this.userService.getFollowingById(this.user.id).subscribe(
      {
        next: ((following: UserShortInfoModel[]) => {
          this.following = following;
        }),

        error: (error => {
          console.error('Error occurred while fetching following:', error);
        })
      }
    );
  }

  subscribe(user : UserShortInfoModel) {

    this.userService.subscribeOnUser(user.id).subscribe(
      {
        next: (() => {
            this.following.push(user);
        }),

        error: (() => {

        })
      }
    )
  }

  unsubscribe(user : UserShortInfoModel) {

    this.userService.subscribeOnUser(user.id).subscribe(
      {
        next: (() => {
          const index = this.following.indexOf(user);
          if (index !== -1){
            this.following.splice(index, 1);
          }
        }),

        error: (() => {

        })
      }
    )
  }

  //very slow method. Need to rewrite
  isSubscribed(user_id : number): boolean{
    for (const user of this.following) {
      if (user.id == user_id){
        return true;
      }
    }
    return false;
  }

  getUserIdFromUrl(): string | null {
    return this.route.snapshot.paramMap.get('id');
  }

  navigateToUserPage(user_id: number) {
    this.router.navigate(["profile/" + user_id.toString()]);
  }
}
