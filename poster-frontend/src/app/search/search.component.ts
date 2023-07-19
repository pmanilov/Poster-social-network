import { Component } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {UserService} from "../services/user.service";
import {UserShortInfoModel} from "../models/user-short-info.model";

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent {
  query: string = '';
  users: UserShortInfoModel[] = [];
  constructor(private route: ActivatedRoute,
              private userService: UserService) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
        this.query = String(params.get('query'));
    });
    this.loadSearchUsers()
  }

  loadSearchUsers(): void {
    this.userService.searchUsers(this.query).subscribe(
      {
        next:  ((users : UserShortInfoModel[]) => {
        this.users = users;
      }),
        error: (error => {
          console.error('Error occurred while search:', error);
        })
      }
    );
  }
}
