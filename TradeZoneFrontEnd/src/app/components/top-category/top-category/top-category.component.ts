import { Component, OnInit, Input } from '@angular/core';
import { TopCategoryListInfo } from 'src/app/core/models/top-category';

@Component({
  selector: 'app-top-category',
  templateUrl: './top-category.component.html',
  styleUrls: ['./top-category.component.css']
})
export class TopCategoryComponent implements OnInit {

  @Input() category: TopCategoryListInfo

  @Input() config: any;

  constructor() { }

  ngOnInit(): void {
  }

  changePage(event) {
    this.config.currentPage = event;
  }
}
