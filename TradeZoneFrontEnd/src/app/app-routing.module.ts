import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { AuthGuard } from './core/guards/auth.guard';
import { AnonymGuard } from './core/guards/anonym.guard';
import { ErrorComponent } from './components/error/error.component';

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
    path: 'advertisements',
    loadChildren: './components/advertisement/advertisement.module#AdvertisementModule', canLoad: [AuthGuard]
  },

  {
    path: 'auth',
    loadChildren: './components/auth/auth.module#AuthModule', canLoad: [AnonymGuard]
  },

  {
    path: 'regions',
    loadChildren: './components/regions/regions.module#RegionsModule',
  },
  {
    path: 'error/:message',
    component: ErrorComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
