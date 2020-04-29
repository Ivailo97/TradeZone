export class AdvertisementEditedModel {

    id: number
    title: string;
    description: string;
    price: number;
    condition: string;
    category: number;
    editor: string;

    constructor(id, title, description, price, condition, category, editor) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.condition = condition;
        this.category = category;
        this.editor = editor;
    }
}