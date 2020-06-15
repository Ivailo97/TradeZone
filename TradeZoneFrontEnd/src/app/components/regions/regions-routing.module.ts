import { RouterModule, Routes } from '@angular/router';
import { RegionsListComponent } from './regions-list/regions-list.component';

const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    component: RegionsListComponent
  },

];

export const RegionsRoutingModule = RouterModule.forChild(routes);