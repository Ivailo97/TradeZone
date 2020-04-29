import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './components/shared/home/home.component';
import { AuthGuard } from './core/guards/auth.guard';
import { AnonymGuard } from './core/guards/anonym.guard';

const routes: Routes = [

  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'home',
  },
  {
    path: 'home',
    component: HomeComponent
  },

  {
    path: 'user',
    loadChildren: './components/user/user.module#UserModule', canLoad: [AuthGuard]
  },
  {
    path: 'advertisement',
    loadChildren: './components/advertisement/advertisement.module#AdvertisementModule', canLoad: [AuthGuard]
  },

  {
    path: 'auth',
    loadChildren: './components/auth/auth.module#AuthModule', canLoad: [AnonymGuard]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
