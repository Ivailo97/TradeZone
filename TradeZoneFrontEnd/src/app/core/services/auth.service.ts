import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, Subject } from 'rxjs';
import { AuthLoginInfo } from '../models/login-info';
import { JwtResponse } from '../models/jwt-response';
import { SignUpInfo } from '../models/signup-info';
import { TokenStorageService } from './token-storage.service';
import { ConversationUser } from 'src/app/components/user/messages-modal/messages-modal.component';
import { tap } from 'rxjs/operators';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

const apiBaseUrl = 'http://localhost:8080';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private loginUrl = apiBaseUrl + '/api/auth/signin';
  private signupUrl = apiBaseUrl + '/api/auth/signup';
  private getUsersUrl = apiBaseUrl + '/api/auth/listUsers'

  constructor(
    private http: HttpClient,
    private tokenService: TokenStorageService) { }

  attemptAuth(credentials: AuthLoginInfo): Observable<JwtResponse> {
    return this.http.post<JwtResponse>(this.loginUrl, credentials, httpOptions);
  }

  signUp(info: SignUpInfo): Observable<string> {
    return this.http.post<string>(this.signupUrl, info, httpOptions);
  }

  isAuthenticated(): boolean {
    return this.tokenService.getToken() !== undefined && this.tokenService.getToken() !== null;
  }

  isAdmin(): boolean {
    return this.tokenService.getAuthorities().includes('ROLE_ADMIN');
  }

  findUsers(): Observable<Array<ConversationUser>> {
    return this.http.get<Array<ConversationUser>>(this.getUsersUrl);
  }
 
}