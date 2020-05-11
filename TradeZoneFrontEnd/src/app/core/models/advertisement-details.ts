import { Image } from "./image";
import { Creator } from "./creator";

export interface AdvertisementDetails {
  id: number;
  title: string;
  images: Image[];
  description: string;
  price: number;
  condition: string;
  views: number;
  creator: Creator;
  delivery: string
  createdOn: any;
  categoryName: string;
}