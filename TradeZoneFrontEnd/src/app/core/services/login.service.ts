import { Injectable } from '@angular/core';
import { Subject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  private subject = new Subject<any>();

  sendRoles(roles: string[]) {
    this.subject.next({ roles: roles })
  }

  getRoles(): Observable<string[]> {
    return this.subject.asObservable();
  }

  clearRoles(): void {
    this.subject.next('logged out')
  }
}
