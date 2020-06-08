import { Component, OnInit } from '@angular/core';
import { SignUpInfo } from '../../../core/models/signup-info';
import { AuthService } from 'src/app/core/services/auth.service';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MustMatch } from '../../../core/helpers/matchValidator';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  form: FormGroup;
  signupInfo: SignUpInfo;
  errorMessage = '';

  constructor(private fb: FormBuilder,
    private authService: AuthService,
    private router: Router) { }

  ngOnInit() {

    this.form = this.fb.group({
      username: ['', [Validators.required, Validators.pattern(/^[a-z][a-z0-9]{2,11}$/)]],
      email: ['', [Validators.required, Validators.pattern(/^\w+@[a-zA-Z_]+?\.[a-zA-Z]{2,3}$/)]],
      password: ['', [Validators.required, Validators.pattern(/^[A-Za-z0-9]{3,16}$/)]],
      confirmPassword: ['', Validators.required],
    }, { validator: MustMatch('password', 'confirmPassword') })

    
  }
  register() {

    this.signupInfo = new SignUpInfo(
      this.f.username.value,
      this.f.email.value,
      this.f.password.value);

    this.authService
      .signUp(this.signupInfo)
      .subscribe((data) => {
        console.log(data);
        this.router.navigate(['/auth/signin']);
      },
        error => {
          console.log(error);
          this.errorMessage = error.error.message;
        })
  }

  get f() {
    return this.form.controls;
  }
}