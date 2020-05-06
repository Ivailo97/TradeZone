import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminComponent } from './admin/admin.component';
import { UserRoutingModule } from './user-routing';
import { ProfileComponent } from './profile/profile.component';
import { MatInputModule } from '@angular/material/input';
import { MatTabsModule } from '@angular/material/tabs';
import { AdvertisementModule } from '../advertisement/advertisement.module';
import { ReactiveFormsModule } from '@angular/forms';
import {MatProgressBarModule} from '@angular/material/progress-bar';
import { ReplacePipe } from 'src/app/core/helpers/replace.pipe';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';


@NgModule({
  declarations: [
    AdminComponent,
    ProfileComponent,
    ReplacePipe,
 
  ],
  imports: [
    CommonModule,
    MatProgressBarModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatInputModule,
    UserRoutingModule,
    ReactiveFormsModule,
    MatTabsModule,
    AdvertisementModule
  ],
})
export class UserModule { }