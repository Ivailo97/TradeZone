import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from "@angular/router";
import { Injectable } from "@angular/core";
import { AuthService } from "../services/auth.service";

@Injectable({
    providedIn: 'root'
})
export class AdminGuard implements CanActivate {

    constructor(
        private authService: AuthService,
        private router: Router
    ) { }


    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {

        if(!this.authService.isAdmin()){

            this.router.navigate(['home']);

            return false;
        }

        return true;

    };







}