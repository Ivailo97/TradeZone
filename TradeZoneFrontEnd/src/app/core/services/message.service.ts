import { Injectable } from '@angular/core';
import { Message } from 'src/app/components/user/messages-modal/messages-modal.component';
import { Subject, Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

export const settings = { baseUrl: 'http://localhost:8080' }

const byDateAsc = (m1, m2) => {
    if (m1.timestamp > m2.timestamp) {
        return 1;
    }
    return -1;
}

@Injectable({
    providedIn: 'root'
})
export class MessageService {

    private messages: Array<Message>;

    private msgs = new Subject<Array<Message>>();

    constructor(private http: HttpClient) { }

    pushMessage(message: Message) {
        this.messages.push(message);
        this.msgs.next(this.messages);
    }

    private getMessagesForChanel(channelId: string): Observable<Array<Message>> {
        return this.http.get<Array<Message>>(`${settings.baseUrl}/api/messages/${channelId}`);
    }

    filterMessages(channel: string): void {

        this.getMessagesForChanel(channel)
            .subscribe(
                data => {
                    console.log("im first here");
                    this.messages = data['content'].sort(byDateAsc);;
                    this.msgs.next(this.messages);
                }
            );
    }

    sendReadReceipt(channelId: string, sender: string) {

        this.http.post(`${settings.baseUrl}/api/messages/read`, {
            channelId: channelId,
            username: sender
        })
            .subscribe(data => {
                console.log(data);
            });
    }

    getMessages(): Observable<Array<Message>> {
        return this.msgs.asObservable();
    }
}
