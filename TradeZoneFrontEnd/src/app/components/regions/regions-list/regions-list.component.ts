import { Component, OnInit } from '@angular/core';
import { RegionService } from 'src/app/core/services/region.service';
import { Observable } from 'rxjs';
import { Region } from 'src/app/core/models/region';

@Component({
  selector: 'app-regions-list',
  templateUrl: './regions-list.component.html',
  styleUrls: ['./regions-list.component.css']
})
export class RegionsListComponent implements OnInit {

  regions$: Observable<Array<Region>>

  constructor(private regionService: RegionService) { }

  ngOnInit(): void {
    this.regions$ = this.regionService.all();
  }

}
