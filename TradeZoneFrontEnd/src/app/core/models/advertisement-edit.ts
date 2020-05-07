export class AdvertisementEditedModel {

    id: number
    title: string;
    description: string;
    price: number;
    condition: string;
    delivery: string;
    category: number;
    creator: string;

    constructor(id: number, title: string, description: string, price: number,
        condition: string, delivery: string, category: number, creator: string) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.condition = condition;
        this.delivery = delivery;
        this.category = category;
        this.creator = creator;
    }
}