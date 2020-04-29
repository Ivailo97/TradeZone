import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserData } from 'src/app/components/user/admin/admin.component';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private adminUrl = 'http://localhost:8080/api/user/admin';

  constructor(private http: HttpClient) {}

  getAdminBoard(): Observable<UserData[]> {
    return this.http.get<UserData[]>(this.adminUrl);
  }
}
