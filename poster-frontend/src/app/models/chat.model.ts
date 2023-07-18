import { MessageModel } from "./message.model";
import {UserShortInfoModel} from "./user-short-info.model";

export interface ChatModel {
  id: number;
  firstUser: UserShortInfoModel;
  secondUser: UserShortInfoModel;
  messages: MessageModel[];
}
