import { Component, OnInit, ViewChild } from '@angular/core';
import { RegionService } from 'src/app/core/services/region.service';
import { Region } from 'src/app/core/models/region';
import { MatAccordion } from '@angular/material/expansion';
import { Town } from 'src/app/core/models/town';

@Component({
  selector: 'app-regions-list',
  templateUrl: './regions-list.component.html',
  styleUrls: ['./regions-list.component.css']
})
export class RegionsListComponent implements OnInit {

  @ViewChild(MatAccordion) accordion: MatAccordion;

  regions: Array<Region>

  constructor(private regionService: RegionService) { }

  ngOnInit(): void {
    this.regionService.all().subscribe(regions => this.regions = regions);
  }

  public loadTowns(regionName: string): void {
    const region = this.regions.find(r => r.name === regionName);
    if(!region.towns){
      this.regionService.getTownsInRegion(regionName).subscribe(towns => region.towns = towns);
    }
  }
}
