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

  constructor(private http: HttpClient, private profileService: ProfileService) { }

  get refreshNeeded$() {
    return this._refreshNeeded$;
  }

  getAllAdvertisementsWithPriceBetween(min, max, page, sortProperty, sortOrder, condition, category): Observable<AdvertisementInfoList[]> {
    return this.http.get<AdvertisementInfoList[]>(`${this.baseURL}/all/${category}?min=${min}&max=${max}&page=${page}&sortBy=${sortProperty}&order=${sortOrder}&condition=${condition}`);
  }

  getAdvertisementsByTitleAndPriceBetween(title: string, min: number, max: number): Observable<AdvertisementInfoList[]> {
    return this.http.get<AdvertisementInfoList[]>(`${this.baseURL}/filter?title=${title}&min=${min}&max=${max}`);
  }

  getAllByCategoryAndPriceBetween(category, min, max) {
    return this.http.get<AdvertisementInfoList[]>(`${this.baseURL}/${category}?min=${min}&max=${max}`);
  }

  getAdvertisement(id) {
    return this.http.get<AdvertisementDetails>(`${this.baseURL}/details/${id}`);
  }

  getAdvertisementsByTitleContainingCategoryPriceBetweenAndCondition(title, category, min, max, page, sortProperty, sortOrder, condition): Observable<AdvertisementInfoList[]> {
    return this.http.get<AdvertisementInfoList[]>(`${this.baseURL}/category-and-text?search=${title}&category=${category}&min=${min}&max=${max}&page=${page}&sortBy=${sortProperty}&order=${sortOrder}&condition=${condition}`);
  }

  createAdvertisement(advertisement: AdvertisementBindingModel): Observable<string> {
    return this.http.post<string>(`${this.baseURL}/create`, advertisement, httpOptions)
      .pipe(
        tap(() => {
          this._refreshNeeded$.next();
        })
      );
  }

  updateViews(id: number, updatedViews) {

    this.http.patch(`${this.baseURL}/increase-views/${id}`, { views: updatedViews })
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

  getAllCategories(): Observable<CategorySelectModel[]> {
    return this.http.get<CategorySelectModel[]>(`${this.baseURL}/category/select`);
  }

  delete(id: number, user: string): Observable<string> {
    return this.http.delete<string>(`${this.baseURL}/delete/${user}/${id}`)
      .pipe(
        tap(() => {
          this._refreshNeeded$.next();
        })
      );;
  }

  deletePhoto(photoId: number, advertisementId: number, user: string): Observable<string> {
    return this.http.delete<string>(`${this.baseURL}/delete-image/${advertisementId}/${user}/${photoId}`)
      .pipe(
        tap(() => {
          this._refreshNeeded$.next();
        })
      );
  }

  uploadPhotos(advertisementId: number, images: string[], user: string): Observable<string> {
    return this.http.patch<string>(`${this.baseURL}/upload-images/${advertisementId}/${user}`, { images })
      .pipe(
        tap(() => {
          this._refreshNeeded$.next();
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

  getTotalCount(): Observable<number> {
    return this.http.get<number>(`${this.baseURL}/count`)
  }

  getCountByPriceBetween(min, max): Observable<number> {
    return this.http.get<number>(`${this.baseURL}/count-price-between?min=${min}$max=${max}`)
  }

  getCountByPriceBetweenAndCondition(min, max, condition): Observable<number> {
    return this.http.get<number>(`${this.baseURL}/count-price-between-condition?min=${min}&max=${max}&condition=${condition}`)
  }

  getCountByCategoryPriceBetweenAndCondition(currentCategory: string, min: number, max: number, condition: string): Observable<number> {
    return this.http.get<number>(`${this.baseURL}/count-category?category=${currentCategory}&min=${min}&max=${max}&condition=${condition}`);
  }

  getCountBeforeSearch(min: number, max: number, condition: string, category: string, search: string): Observable<number> {
    return this.http.get<number>(`${this.baseURL}/count-price-between-condition-search-category?category=${category}&min=${min}&max=${max}&condition=${condition}&search=${search}`);
  }
}