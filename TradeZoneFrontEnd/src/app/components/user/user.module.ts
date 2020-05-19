import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminComponent } from './admin/admin.component';
import { UserRoutingModule } from './user-routing';
import { ProfileComponent } from './profile/profile.component';
import { MatInputModule } from '@angular/material/input';
import { MatTabsModule } from '@angular/material/tabs';
import { AdvertisementModule } from '../advertisement/advertisement.module';
import { ReactiveFormsModule } from '@angular/forms';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { ReplacePipe } from 'src/app/core/helpers/replace.pipe';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { MessagesModalComponent } from './messages-modal/messages-modal.component';
import { ConversationModalComponent } from './conversation-modal/conversation-modal.component';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { StompService } from 'ng2-stomp-service';

@NgModule({
  declarations: [
    AdminComponent,
    MessagesModalComponent,
    ProfileComponent,
    ReplacePipe,
    ConversationModalComponent
  ],
  imports: [
    CommonModule,
    MatSnackBarModule,
    MatProgressBarModule,
    MatGridListModule,
    MatInputModule,
    MatButtonModule,
    MatListModule,
    MatIconModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    UserRoutingModule,
    ReactiveFormsModule,
    MatTabsModule,
    AdvertisementModule
  ],
  providers: [StompService]
})
export class UserModule { }
