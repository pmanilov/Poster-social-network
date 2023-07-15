import {Data} from "@angular/router";
import {UserShortInfoModel} from "./user-short-info.model";

export interface CommentModel {
  post_id: number
  text: string;
  date: Data;
  user: UserShortInfoModel;
}

