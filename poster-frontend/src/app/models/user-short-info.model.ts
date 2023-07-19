import {ImageDataModel} from "./image-data.model";

export interface UserShortInfoModel {
  id: number;
  username: string;
  hasPhoto: boolean;
  image: ImageDataModel;
}
