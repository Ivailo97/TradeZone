import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { TopCategoryListInfo } from 'src/app/core/models/top-category';
import { CategoryService } from 'src/app/core/services/category.service';

const numberOfCategories = 3;

@Component({
  selector: 'app-top-category-list',
  templateUrl: './top-category-list.component.html',
  styleUrls: ['./top-category-list.component.css']
})
export class TopCategoryListComponent implements OnInit {

  topCategories$: Observable<TopCategoryListInfo[]>;

  constructor(private categoryService:CategoryService) { }

  ngOnInit(): void {
    this.topCategories$ = this.categoryService.getTopCategories(numberOfCategories);
  }

}
