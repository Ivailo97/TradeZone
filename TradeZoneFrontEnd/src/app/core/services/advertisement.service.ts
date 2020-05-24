import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { tap } from 'rxjs/operators'
import { AdvertisementInfoList } from '../models/advertisement-info-list';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AdvertisementDetails } from '../models/advertisement-details';
import { AdvertisementBindingModel } from '../models/advertisement-create';
import { CategorySelectModel } from '../models/category-select';
import { AdvertisementEditedModel } from '../models/advertisement-edit';
import { AdvertisementToEditModel } from '../models/advertisement-to-edit';
import { ProfileService } from './profile.service';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};
@Injectable({
  providedIn: 'root'
})

export class AdvertisementService {

  private baseURL = 'http://localhost:8080/api';

  private _refreshNeeded$ = new Subject<void>();

  private _detailsRefreshNeeded$ = new Subject<void>();

  constructor(private http: HttpClient, private profileService: ProfileService) { }

  get refreshNeeded$() {
    return this._refreshNeeded$;
  }

  get detailsRefreshNeeded$() {
    return this._detailsRefreshNeeded$;
  }

  getCreatedByUser(username: string, page: number, excludeId: number): Observable<AdvertisementInfoList[]> {
    return this.http.get<AdvertisementInfoList[]>(`${this.baseURL}/created-by/${username}?page=${page}&excludeId=${excludeId}`);
  }

  getAdvertisement(id: number): Observable<AdvertisementDetails> {
    return this.http.get<AdvertisementDetails>(`${this.baseURL}/details/${id}`);
  }

  getByFullSearch(params: any): Observable<AdvertisementInfoList[]> {
    return this.http.get<AdvertisementInfoList[]>(`${this.baseURL}/search`, { params: params });
  }

  createAdvertisement(advertisement: AdvertisementBindingModel): Observable<string> {
    return this.http.post<string>(`${this.baseURL}/create`, advertisement, httpOptions)
      .pipe(
        tap(() => {
          this._refreshNeeded$.next();
        })
      );
  }

  updateViews(id: number, views: number, username: string) {

    this.http.patch(`${this.baseURL}/increase-views/${id}`, { views, username })
      .subscribe(
        (val) => {
          console.log("PATCH call successful value returned in body", val);
        },
        response => {
          console.log("PATCH call in error", response);
        },
      );
  }

  getAllConditions(): Observable<String[]> {
    return this.http.get<String[]>(`${this.baseURL}/conditions/all`);
  }

  getAllDeliveries(): Observable<String[]> {
    return this.http.get<String[]>(`${this.baseURL}/deliveries/all`);
  }

  getAllCategories(): Observable<CategorySelectModel[]> {
    return this.http.get<CategorySelectModel[]>(`${this.baseURL}/category/select`);
  }

  delete(params: any): Observable<string> {
    return this.http.delete<string>(`${this.baseURL}/delete`, { params })
      .pipe(
        tap(() => {
          this._refreshNeeded$.next();
          this.profileService.refreshNeeded$.next();
        })
      );;
  }

  deletePhoto(params: any): Observable<string> {
    return this.http.delete<string>(`${this.baseURL}/delete-image`, { params })
      .pipe(
        tap(() => {
          this._refreshNeeded$.next();
          this._detailsRefreshNeeded$.next();
        })
      );
  }

  uploadPhotos(params: any): Observable<string> {
    return this.http.patch<string>(`${this.baseURL}/upload-images`, params)
      .pipe(
        tap(() => {
          this._refreshNeeded$.next();
          this._detailsRefreshNeeded$.next();
        })
      );;
  }

  edit(advertisement: AdvertisementEditedModel): Observable<string> {

    return this.http.put<string>(`${this.baseURL}/edit`, advertisement)
      .pipe(
        tap(() => {
          this._refreshNeeded$.next();
          this.profileService.refreshNeeded$.next();
        })
      );;
  }

  getAdvertisementToEdit(id: number): Observable<AdvertisementToEditModel> {
    return this.http.get<AdvertisementToEditModel>(`${this.baseURL}/edit/${id}`);
  }

  getCountBySearch(params: any): Observable<number> {
    return this.http.get<number>(`${this.baseURL}/count`, { params: params });
  }
}