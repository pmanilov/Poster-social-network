import {UserShortInfoModel} from "./user-short-info.model";

export interface MessageModel {
  id: number;
  text: string;
  date: string;
  sender: UserShortInfoModel;
}
