import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Region } from '../models/region';
import { HttpClient } from '@angular/common/http';
import { Town } from '../models/town';
import { TownInRegion } from '../models/town-in-region';

const baseURL = 'http://localhost:8080/api';

@Injectable({
  providedIn: 'root'
})
export class RegionService {

  constructor(private http: HttpClient) { }

  public all(): Observable<Array<Region>> {
    return this.http.get<Array<Region>>(`${baseURL}/region/all`);
  }

  public getTownsInRegion(regionName: string): Observable<Array<TownInRegion>> {
    return this.http.get<Array<TownInRegion>>(`${baseURL}/towns/region?regionName=${regionName}`);
  }
}
