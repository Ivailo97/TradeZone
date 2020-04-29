import { RouterModule, Routes } from '@angular/router';
import { AdvertisementListComponent } from './advertisement-list/advertisement-list.component';
import { AdvertisementDetailsComponent } from './advertisement-details/advertisement-details.component';
import { SingleAdvertisementResolver } from 'src/app/core/services/resolvers/single-advertisement.service';

const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    component: AdvertisementListComponent
  },

  {
    path: 'all',
    component: AdvertisementListComponent
  },

  {
    path: 'details/:id',
    component: AdvertisementDetailsComponent,
    resolve: { 'singleAdvertisement': SingleAdvertisementResolver }
  },
];

export const AdvertisementRoutingModule = RouterModule.forChild(routes);