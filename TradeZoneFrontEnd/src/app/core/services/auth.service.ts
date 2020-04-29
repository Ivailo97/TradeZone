import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {AuthLoginInfo} from '../models/login-info';
import {JwtResponse} from '../models/jwt-response';
import {SignUpInfo} from '../models/signup-info';
import {TokenStorageService} from './token-storage.service';

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

const apiBaseUrl = 'http://localhost:8080';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private loginUrl = apiBaseUrl + '/api/auth/signin';
  private signupUrl = apiBaseUrl + '/api/auth/signup';

  constructor(
    private http: HttpClient,
    private tokenService: TokenStorageService
  ) {}

  attemptAuth(credentials: AuthLoginInfo): Observable<JwtResponse> {
    return this.http.post<JwtResponse>(this.loginUrl, credentials, httpOptions);
  }

  signUp(info: SignUpInfo): Observable<string> {
    return this.http.post<string>(this.signupUrl, info, httpOptions);
  }

  isAuthenticated() {
    return this.tokenService.getToken() !== null;
  }
}