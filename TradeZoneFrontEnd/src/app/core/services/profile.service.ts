import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { TokenStorageService } from './token-storage.service';
import { UserProfile } from '../models/user-profile';
import { Observable, Subject } from 'rxjs';
import { ProfileUpdate } from '../models/profile-update';
import { tap } from 'rxjs/operators';
import { PasswordUpdate } from '../models/password-update';
import { Message } from '../models/message';
import { ConversationUser } from 'src/app/components/user/messages-modal/messages-modal.component';

const baseURL = 'http://localhost:8080/api/user/profile';
const disconnectUrl = baseURL + '/disconnect';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

const httpFileOptions = {
  headers: new HttpHeaders({ 'enctype': 'multipart/form-data' })
};

@Injectable({
  providedIn: 'root'
})
export class ProfileService {

  private _refreshNeeded$ = new Subject<void>();

  private _subscribedTo: Array<ConversationUser> = [];

  constructor(private http: HttpClient,
    private tokenStorageService: TokenStorageService) { }

  getUserProfile(): Observable<UserProfile> {
    return this.http.get<UserProfile>(`${baseURL}?username=${this.tokenStorageService.getUsername()}`);
  }

  sendMessage(message: Message): Observable<void> {
    return this.http.patch<void>(`${baseURL}/send-message`, message);
  }

  addFavoriteAdvertisement(advertisementId: number): Observable<string> {
    const requestBody = { username: this.tokenStorageService.getUsername(), advertisementId };
    return this.http.post<string>(`${baseURL}/add-favorite`, requestBody).pipe(
      tap(() => {
        this._refreshNeeded$.next();
      })
    );
  }

  isCompleted(): Observable<boolean> {
    return this.http.get<boolean>(`${baseURL}/is-completed?username=${this.tokenStorageService.getUsername()}`);
  }

  removeFavoriteAdvertisement(advertisementId: number): Observable<string> {
    const requestBody = { username: this.tokenStorageService.getUsername(), advertisementId };
    return this.http.post<string>(`${baseURL}/remove-favorite`, requestBody).pipe(
      tap(() => {
        this._refreshNeeded$.next();
      })
    );;;;
  }

  update(updatedModel: ProfileUpdate): Observable<string> {
    return this.http.patch<string>(`${baseURL}/update`, updatedModel)
      .pipe(
        tap(() => {
          this._refreshNeeded$.next();
        })
      );;;
  }

  updatePassword(passwordUpdate: PasswordUpdate): Observable<string> {
    return this.http.patch<string>(`${baseURL}/update-password`, passwordUpdate)
  }

  uploadPhoto(file: FormData): Observable<string> {
    return this.http.patch<string>(`${baseURL}/update-picture/${this.tokenStorageService.getUsername()}`, file, httpFileOptions)
      .pipe(
        tap(() => {
          this._refreshNeeded$.next();
        })
      );;;;
  }

  disconnect() {
    const username = this.tokenStorageService.getUsername();
    return this.http.post<void>(disconnectUrl, username);
  }

  get refreshNeeded$() {
    return this._refreshNeeded$;
  }

  get subscribedTo() {
    return this._subscribedTo;
  }

  public addToSubscribed(user: ConversationUser): void {
    this.subscribedTo.push(user);
  }

  public isSubscribedTo(user: ConversationUser): boolean {
    return this.subscribedTo.find(x => x.id === user.id) !== undefined;
  }
}
