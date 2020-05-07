export class AdvertisementToEditModel {

    title: string;
    description: string;
    price: number;
    condition: string;
    delivery: string
    categoryId: number;
    creator: string;

    constructor(title, description, price, condition, delivery, categoryId, creator) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.condition = condition;
        this.delivery = delivery;
        this.categoryId = categoryId;
        this.creator = creator;
    }
}