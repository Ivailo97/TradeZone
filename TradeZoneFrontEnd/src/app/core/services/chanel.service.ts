import { Injectable } from '@angular/core';
import { Subject, Observable } from 'rxjs';
import {Md5} from 'ts-md5/dist/md5';

@Injectable({
  providedIn: 'root'
})
export class ChanelService {

  private channel = new Subject<string>();

  public static createChannel(user1: string, user2: string): string {
    let combined: string = '';

    if (user1 < user2) {
      combined = user1 + user2;
    } else {
      combined = user2 + user1;
    }

    return Md5.hashStr(combined).toString();
  }

  refreshChannel(channel: string) {
    this.channel.next(channel);
  }

  removeChannel() {
    this.channel.next();
  }

  getChannel(): Observable<any> {
    return this.channel.asObservable();
  }
}
