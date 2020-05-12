import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { TokenStorageService } from './token-storage.service';
import { UserProfile } from '../models/user-profile';
import { Observable, Subject } from 'rxjs';
import { ProfileUpdate } from '../models/profile-update';
import { tap } from 'rxjs/operators';
import { PasswordUpdate } from '../models/password-update';

const baseURL = 'http://localhost:8080/api/user/profile';

const httpFileOptions = {
  headers: new HttpHeaders({ 'enctype': 'multipart/form-data' })
};

@Injectable({
  providedIn: 'root'
})
export class ProfileService {

  private _refreshNeeded$ = new Subject<void>();

  constructor(private http: HttpClient,
    private tokenStorageService: TokenStorageService) { }

  getUserProfile(): Observable<UserProfile> {
    return this.http.get<UserProfile>(`${baseURL}?username=${this.tokenStorageService.getUsername()}`);
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

  get refreshNeeded$() {
    return this._refreshNeeded$;
  }
}
