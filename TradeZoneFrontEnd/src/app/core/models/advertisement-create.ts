export class AdvertisementBindingModel {

    title: string;
    description: string;
    images: string[];
    price: number;
    condition: string;
    category: number;
    creator: string;

    constructor(title, description, images, price, condition, category, creator) {

        this.title = title;
        this.description = description;
        this.images = images;
        this.price = price;
        this.condition = condition;
        this.category = category;
        this.creator = creator;
    }
}