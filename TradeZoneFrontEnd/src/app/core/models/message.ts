export class Message {

    content: string;
    sender: string;
    receiver: string;

    constructor(content: string, sender: string, receiver: string) {
        this.content = content;
        this.sender = sender;
        this.receiver = receiver;
    }
}