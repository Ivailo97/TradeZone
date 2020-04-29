export class PasswordUpdate {
    id: number
    oldPassword: string;
    newPassword: string;
    confirmNewPassword: string;

    constructor(id: number, oldPassword: string, newPassword: string, confirmNewPassword: string) {
        this.id = id;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.confirmNewPassword = confirmNewPassword;
    }
}
