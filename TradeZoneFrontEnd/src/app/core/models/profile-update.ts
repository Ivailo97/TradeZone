export class ProfileUpdate {
    id: number
    firstName: string;
    lastName: string;
    city: number;
    aboutMe: string;

    constructor(id: number, firstName: string, lastName: string, city: number, aboutMe: string) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.aboutMe = aboutMe;
    }
}
