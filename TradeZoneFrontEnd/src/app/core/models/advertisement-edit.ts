export class AdvertisementEditedModel {

    id: number
    title: string;
    description: string;
    price: number;
    condition: string;
    category: number;
    creator: string;

    constructor(id, title, description, price, condition, category, creator) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.condition = condition;
        this.category = category;
        this.creator = creator;
    }
}