import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Region } from '../models/region';
import { HttpClient } from '@angular/common/http';

const baseURL = 'http://localhost:8080/api';

@Injectable({
  providedIn: 'root'
})
export class RegionService {

  constructor(private http: HttpClient) { }

  public all(): Observable<Array<Region>> {
    return this.http.get<Array<Region>>(`${baseURL}/region/all`);
  }
}
