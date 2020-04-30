import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CategoryInfoComponent } from './category-info/category-info.component';
import { CategoryListComponent } from './category-list/category-list.component';
import { CategoryModalCreateComponent } from './category-modal-create/category-modal-create.component';
import { MatDividerModule } from '@angular/material/divider';
import { ReactiveFormsModule } from '@angular/forms';

@NgModule({
  declarations: [
    CategoryInfoComponent,
    CategoryListComponent,
    CategoryModalCreateComponent,
  ],
  imports: [
    CommonModule,
    MatDividerModule,
    ReactiveFormsModule
  ],
  exports: [
    CategoryInfoComponent,
    CategoryListComponent,
    CategoryModalCreateComponent,
  ]
})
export class CategoryModule { }
