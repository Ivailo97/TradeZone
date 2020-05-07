export class AdvertisementBindingModel {

    title: string;
    description: string;
    images: string[];
    price: number;
    condition: string;
    category: number;
    delivery: string
    creator: string;

    constructor(title, description, images, price, condition, category, delivery, creator) {

        this.title = title;
        this.description = description;
        this.images = images;
        this.price = price;
        this.condition = condition;
        this.category = category;
        this.delivery = delivery;
        this.creator = creator;
    }
}