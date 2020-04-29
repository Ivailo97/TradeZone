import { CategoryListInfo } from "./category-info";
import { AdvertisementInfoList } from "./advertisement-info-list";

export class UserProfile {
    id:number;
    firstName:string;
    lastName:string;
    userUsername: string;
    roles:string[];
    city:string;
    country:string;
    isCompleted:boolean;
    aboutMe:string;
    photoUrl:string;
    userEmail: string;
    createdCategories: Array<CategoryListInfo>;
    createdAdvertisements: Array<AdvertisementInfoList>;
    favorites:Array<AdvertisementInfoList>;
}