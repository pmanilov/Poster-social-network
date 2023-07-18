import {Data} from "@angular/router";
import {UserShortInfoModel} from "./user-short-info.model";
import {CommentModel} from "./comment.model";

export interface PostModel {
  id: number;
  text: string;
  date: Data;
  amountOfLikes: number;
  amountOfComments: number;
  showComments: boolean;
  user: UserShortInfoModel;
  comments: CommentModel[];
  likedByCurrentUser: boolean;
}
