export interface AdvertisementInfoList {
  id: number;
  title: string;
  imageUrl: string;
  price: number;
  condition:string;
  description:string;
  views:number;
  creator:string;
  profilesWhichLikedIt:string[];
}