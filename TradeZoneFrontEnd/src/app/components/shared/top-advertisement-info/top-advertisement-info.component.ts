import { Component, OnInit, Input } from '@angular/core';
import { AdvertisementInfoList } from 'src/app/core/models/advertisement-info-list';

@Component({
  selector: 'app-top-advertisement-info',
  templateUrl: './top-advertisement-info.component.html',
  styleUrls: ['./top-advertisement-info.component.css']
})
export class TopAdvertisementInfoComponent implements OnInit {

  @Input() advertisement: AdvertisementInfoList;

  liked: boolean;

  constructor() { }

  ngOnInit(): void {
    this.liked = false;
  }

  toogleFavorite() {
    this.liked = !this.liked;
  }

}
