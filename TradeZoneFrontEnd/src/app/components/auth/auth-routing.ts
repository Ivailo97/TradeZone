import { RouterModule, Routes } from '@angular/router';
import { RegisterComponent } from './register/register.component';
import { LoginComponent } from './login/login.component';

const routes: Routes = [
    {
        path: '',
        pathMatch: 'full',
        component: RegisterComponent
    },

    {
        path: 'signup',
        component: RegisterComponent
    },

    {
        path: 'signin',
        component: LoginComponent
    },

];

export const AuthRoutingModule = RouterModule.forChild(routes);