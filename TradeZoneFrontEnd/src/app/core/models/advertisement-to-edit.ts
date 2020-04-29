export class AdvertisementToEditModel {

    title: string;
    description: string;
    price: number;
    condition: string;
    categoryId: number;
    creator: string;

    constructor( title, description, price, condition, categoryId, creator) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.condition = condition;
        this.categoryId = categoryId;
        this.creator = creator;
    }
}