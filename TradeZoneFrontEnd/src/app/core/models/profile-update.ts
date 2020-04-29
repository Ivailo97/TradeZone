export class ProfileUpdate {
    id: number
    firstName: string;
    lastName: string;
    city: string;
    country: string;
    aboutMe: string;

    constructor(id: number, firstName: string, lastName: string, city: string, country: string, aboutMe: string) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.country = country;
        this.aboutMe = aboutMe;
    }
}
