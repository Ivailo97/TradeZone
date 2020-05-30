export class ProfileUpdate {
    id: number
    firstName: string;
    lastName: string;
    town: number;
    aboutMe: string;

    constructor(id: number, firstName: string, lastName: string, town: number, aboutMe: string) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.town = town;
        this.aboutMe = aboutMe;
    }
}
