import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RegionsListComponent } from './regions-list/regions-list.component';
import { RegionItemComponent } from './region-item/region-item.component';
import { RegionsRoutingModule } from './regions-routing.module';
@NgModule({
  declarations: [RegionsListComponent, RegionItemComponent],
  imports: [
    CommonModule,
    RegionsRoutingModule
  ]
})
export class RegionsModule { }
