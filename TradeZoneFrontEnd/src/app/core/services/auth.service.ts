import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthLoginInfo } from '../models/login-info';
import { JwtResponse } from '../models/jwt-response';
import { SignUpInfo } from '../models/signup-info';
import { TokenStorageService } from './token-storage.service';
import { ConversationUser } from 'src/app/components/user/messages-modal/messages-modal.component';

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
  private logoutUrl = apiBaseUrl + '/api/auth/logout';
  private getUsersUrl = apiBaseUrl + '/api/auth/listUsers'

  constructor(
    private http: HttpClient,
    private tokenService: TokenStorageService
  ) { }

  attemptAuth(credentials: AuthLoginInfo): Observable<JwtResponse> {
    return this.http.post<JwtResponse>(this.loginUrl, credentials, httpOptions);
  }

  signUp(info: SignUpInfo): Observable<string> {
    return this.http.post<string>(this.signupUrl, info, httpOptions);
  }

  isAuthenticated() {
    return this.tokenService.getToken() !== null;
  }

  findUsers(): Observable<Array<ConversationUser>> {
    return this.http.get<Array<ConversationUser>>(this.getUsersUrl);
  }

  logout() {
    return this.http.post<void>(this.logoutUrl, this.tokenService.getUsername(), httpOptions);
  }
}