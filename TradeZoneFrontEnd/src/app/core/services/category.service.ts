import { Injectable } from '@angular/core';
import { CategoryListInfo } from '../models/category-info';
import { Observable, Subject } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CategoryBindingModel } from '../models/category-create';
import { tap } from 'rxjs/operators';
import { TopCategoryListInfo } from '../models/top-category';

const baseURL = 'http://localhost:8080';
const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  private url = `${baseURL}/api/category/all`;

  private _refreshNeeded$ = new Subject<void>();

  constructor(private http: HttpClient) { }

  getAllCategories(): Observable<CategoryListInfo[]> {
    return this.http.get<CategoryListInfo[]>(this.url);
  }

  createCategory(category: CategoryBindingModel): Observable<string> {

    return this.http.post<string>(`${baseURL}/api/category/create`, category, httpOptions)
      .pipe(
        tap(() => {
          this._refreshNeeded$.next();
        })
      );
  }

  getTopCategories(count: number): Observable<TopCategoryListInfo[]> {
    return this.http.get<TopCategoryListInfo[]>(`${baseURL}/api/category/top?count=${count}`);
  }

  get refreshNeeded$() {
    return this._refreshNeeded$;
  }
}
