import { RouterModule, Routes } from '@angular/router';
import { AdminComponent } from './admin/admin.component';
import { ProfileComponent } from './profile/profile.component';
import { AdminGuard } from 'src/app/core/guards/admin.guard';

const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'home'
  },

  {
    path: 'admin',
    component: AdminComponent,
    canActivate: [AdminGuard]
  },

  {
    path: 'profile',
    component: ProfileComponent
  }

];


export const UserRoutingModule = RouterModule.forChild(routes);
