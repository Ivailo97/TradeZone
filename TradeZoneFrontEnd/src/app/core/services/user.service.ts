import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, Subject } from 'rxjs';
import { UserData, RoleChange } from 'src/app/components/user/admin/admin.component';
import { tap } from 'rxjs/operators';

const defaultUrl = 'http://localhost:8080/api';
const adminBoardUrl = `${defaultUrl}/user/admin`;

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private _refreshNeeded$ = new Subject<void>();

  constructor(private http: HttpClient) { }

  getAdminBoard(): Observable<UserData[]> {
    return this.http.get<UserData[]>(adminBoardUrl);
  }

  getAllRoles(): Observable<string[]> {
    return this.http.get<string[]>(`${defaultUrl}/auth/roles`);
  }

  changeRole(roleChange: RoleChange): Observable<void> {
    return this.http.patch<void>(`${defaultUrl}/auth/admin/change-role`, roleChange)
      .pipe(
        tap(() => {
          this._refreshNeeded$.next();
        })
      );;;;;
  }

  get refreshNeeded$() {
    return this._refreshNeeded$;
  }
}
