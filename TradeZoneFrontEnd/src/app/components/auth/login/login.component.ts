import { Component, OnInit } from '@angular/core';
import { AuthLoginInfo } from '../../../core/models/login-info';
import { AuthService } from 'src/app/core/services/auth.service';
import { TokenStorageService } from 'src/app/core/services/token-storage.service';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { LoginService } from 'src/app/core/services/login.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  form: FormGroup;
  errorMessage = '';
  private loginInfo: AuthLoginInfo;

  constructor(private authService: AuthService,
    private tokenStorage: TokenStorageService,
    private router: Router,
    private fb: FormBuilder,
    private logingService: LoginService) { }

  ngOnInit() {
    this.form = this.fb.group({
      username: ['', [Validators.required, Validators.pattern(/[a-z][a-z0-9]{2,11}/)]],
      password: ['', [Validators.required, Validators.pattern(/[A-Za-z0-9]{3,16}/)]],
    })
  }

  login() {

    this.loginInfo = new AuthLoginInfo(
      this.f.username.value,
      this.f.password.value);

    this.authService.attemptAuth(this.loginInfo).subscribe(
      data => {
        this.tokenStorage.saveToken(data['token']);
        this.tokenStorage.saveUsername(data.username);
        this.tokenStorage.saveAuthorities(data.authorities);
        this.logingService.sendRoles(data.authorities);
        this.router.navigate(['/home']);
      },
      error => {
        this.errorMessage = error.error.message;
      }
    );
  }

  get f() {
    return this.form.controls;
  }
}