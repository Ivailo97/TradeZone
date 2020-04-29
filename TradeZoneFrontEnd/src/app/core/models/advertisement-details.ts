import { Image } from "./image";

export interface AdvertisementDetails {
  id:number;
  title: string;
  images: Image[];
  description: string;
  price: number;
  condition: string;
  views: number;
  creator: string;
}