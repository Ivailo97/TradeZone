import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Town } from '../models/town';

const baseUrl = 'http://localhost:8080/api/towns';

@Injectable({
  providedIn: 'root'
})
export class TownService {

  constructor(private http: HttpClient) { }

  public getAll(): Observable<Array<Town>> {
    return this.http.get<Array<Town>>(`${baseUrl}/all`);
  }
}
