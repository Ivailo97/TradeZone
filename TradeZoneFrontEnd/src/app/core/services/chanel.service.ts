import { Injectable } from '@angular/core';
import { Subject, Observable } from 'rxjs';
import { Md5 } from 'ts-md5/dist/md5';
import { HttpClient } from '@angular/common/http';
import { settings } from './message.service';
import { ProfileService } from './profile.service';
import { tap } from 'rxjs/operators';

export class Subscription {
  username: string
  channelId: string

  constructor(username: string, channelId: string) {
    this.username = username;
    this.channelId = channelId;
  }
}

@Injectable({
  providedIn: 'root'
})
export class ChanelService {

  private channel = new Subject<string>();

  constructor(private http: HttpClient,
    private profileService: ProfileService) { }

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

  createChannel(channelId: string): Observable<any> {
    return this.http.post<any>(`${settings.baseUrl}/api/channel/create`, channelId);
  }

  public subscribeToChannel(username: string, channelId: string): Observable<any> {
    return this.http.post<any>(`${settings.baseUrl}/api/channel/subscribe`, new Subscription(username, channelId))
      .pipe(
        tap(() => {
          this.profileService.refreshNeeded$.next();
        })
      );;;
  }

  public unsubscribeFromChannel(username: string, channelId: string): Observable<any> {
    return this.http.post<any>(`${settings.baseUrl}/api/channel/unsubscribe`, new Subscription(username, channelId))
    .pipe(
      tap(() => {
        this.profileService.refreshNeeded$.next();
      })
    );;;
  }
}
