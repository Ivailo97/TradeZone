import { RouterModule, Routes } from '@angular/router';
import { AdminComponent } from './admin/admin.component';
import { ProfileComponent } from './profile/profile.component';

const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'tank'
  },

  {
    path: 'admin',
    component: AdminComponent
  },

  {
    path: 'profile',
    component: ProfileComponent
  }

];


export const UserRoutingModule = RouterModule.forChild(routes);
