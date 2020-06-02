import { CategoryListInfo } from "./category-info";
import { AdvertisementInfoList } from "./advertisement-info-list";
import { Town } from "./town";

export class UserProfile {
    id: number;
    firstName: string;
    lastName: string;
    userUsername: string;
    roles: string[];
    town: Town;
    isCompleted: boolean;
    aboutMe: string;
    photoUrl: string;
    userEmail: string;
    subscribedTo: Array<string>
    createdCategories: Array<CategoryListInfo>;
    createdAdvertisements: Array<AdvertisementInfoList>;
    favorites: Array<AdvertisementInfoList>;
}