import {CanLoad, Route, Router} from '@angular/router';
import {Injectable} from '@angular/core';
import {AuthService} from '../services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanLoad {

  constructor(
    private authService: AuthService,
    private router: Router
  ) { }

  canLoad(route: Route): boolean {
    if (this.authService.isAuthenticated()) {
      return true;
    }

    this.router.navigate(['auth/signin']);
    return false;
  }
}
