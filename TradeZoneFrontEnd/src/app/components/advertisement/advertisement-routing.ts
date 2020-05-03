import { RouterModule, Routes } from '@angular/router';
import { AdvertisementListComponent } from './advertisement-list/advertisement-list.component';
import { AdvertisementDetailsComponent } from './advertisement-details/advertisement-details.component';

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
    component: AdvertisementDetailsComponent
  },
];

export const AdvertisementRoutingModule = RouterModule.forChild(routes);