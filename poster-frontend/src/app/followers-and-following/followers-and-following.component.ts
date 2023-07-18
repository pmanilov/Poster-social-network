import {Component, OnInit} from '@angular/core';
import {UserShortInfoModel} from "../models/user-short-info.model";
import {PostModel} from "../models/post.model";
import {UserService} from "../services/user.service";

@Component({
  selector: 'app-followers-and-following',
  templateUrl: './followers-and-following.component.html',
  styleUrls: ['./followers-and-following.component.css']
})
export class FollowersAndFollowingComponent implements OnInit{
  followers: UserShortInfoModel[] = [];
  following: UserShortInfoModel[] = [];
  selectedTab = "following";

  constructor(
    private userService: UserService
  ) {
  }

  ngOnInit(){
    this.getFollowers();
    this.getFollowing();
  }

  getFollowers(){
    this.userService.getFollowersById(1).subscribe(
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
    this.userService.getFollowingById(1).subscribe(
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

  unsubscribe(subscription: any) {

  }

  navigateToUserPage(follower: any) {

  }
}
