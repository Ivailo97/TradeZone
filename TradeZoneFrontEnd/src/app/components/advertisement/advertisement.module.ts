import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdvertisementListInfoComponent } from './advertisement-list-info/advertisement-list-info.component';
import { AdvertisementListComponent } from './advertisement-list/advertisement-list.component';
import { AdvertisementRoutingModule } from './advertisement-routing';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CategoryInfoComponent } from './category-info/category-info.component';
import { CategoryListComponent } from './category-list/category-list.component';
import { AdvertisementDetailsComponent } from './advertisement-details/advertisement-details.component';
import { AdvertisementModalCreateComponent } from 'src/app/components/advertisement/advertisement-modal-create/advertisement-modal-create.component';
import { PriceRangeComponent } from './price-range/price-range.component';
import { Ng5SliderModule } from 'ng5-slider';
import { CategoryModalCreateComponent } from './category-modal-create/category-modal-create.component';
import { MatDividerModule } from '@angular/material/divider';
import { MatRadioModule } from '@angular/material/radio';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatTooltipModule } from '@angular/material/tooltip';
import { AdvertisementModalDeleteComponent } from './advertisement-modal-delete/advertisement-modal-delete.component';
import { ImageDeleteModalComponent } from './image-delete-modal/image-delete-modal.component';
import { ImageUploadModalComponent } from './image-upload-modal/image-upload-modal.component';
import { AdvertisementModalEditComponent } from './advertisement-modal-edit/advertisement-modal-edit.component';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';


@NgModule({

  declarations: [
    AdvertisementListInfoComponent,
    AdvertisementListComponent,
    AdvertisementDetailsComponent,
    AdvertisementModalCreateComponent,
    CategoryInfoComponent,
    CategoryListComponent,
    PriceRangeComponent,
    CategoryModalCreateComponent,
    AdvertisementModalDeleteComponent,
    ImageDeleteModalComponent,
    ImageUploadModalComponent,
    AdvertisementModalEditComponent,

  ],
  imports: [
    MatSelectModule,
    MatInputModule,
    CommonModule,
    Ng5SliderModule,
    MatIconModule,
    MatDividerModule,
    MatRadioModule,
    MatButtonModule,
    MatToolbarModule,
    MatCheckboxModule,
    AdvertisementRoutingModule,
    MatTooltipModule,
    FormsModule,
    ReactiveFormsModule
  ],
  exports: [
    AdvertisementListInfoComponent,
    AdvertisementModalDeleteComponent,
    AdvertisementModalEditComponent
  ]
})
export class AdvertisementModule {
}
