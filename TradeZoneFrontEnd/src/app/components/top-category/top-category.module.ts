import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TopCategoryComponent } from './top-category/top-category.component';
import { TopCategoryListComponent } from './top-category-list/top-category-list.component';
import { TopAdvertisementInfoComponent } from './top-advertisement-info/top-advertisement-info.component';
import { NgxPaginationModule } from 'ngx-pagination';
import { MatDividerModule } from '@angular/material/divider';
import { SortByLikesPipe } from 'src/app/core/helpers/sort.pipe';
import { MatButtonModule } from '@angular/material/button';
import { RouterModule } from '@angular/router';

@NgModule({
  declarations: [TopCategoryComponent, TopCategoryListComponent, TopAdvertisementInfoComponent, SortByLikesPipe],
  imports: [
    CommonModule,
    RouterModule,
    NgxPaginationModule,
    MatDividerModule,
    MatButtonModule,
  ],
  exports: [TopCategoryListComponent]
})
export class TopCategoryModule { }
