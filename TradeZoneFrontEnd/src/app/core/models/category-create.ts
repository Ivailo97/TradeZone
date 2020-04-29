export class CategoryBindingModel {

    name: string;
    image: string;
    creator: string

    constructor(name, image, creator) {
        this.name = name;
        this.image = image;
        this.creator = creator;
    }
}