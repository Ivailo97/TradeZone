import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { TopCategoryListInfo } from 'src/app/core/models/top-category';
import { CategoryService } from 'src/app/core/services/category.service';
import { ProfileService } from 'src/app/core/services/profile.service';

const numberOfCategories = 3;

@Component({
  selector: 'app-top-category-list',
  templateUrl: './top-category-list.component.html',
  styleUrls: ['./top-category-list.component.css']
})
export class TopCategoryListComponent implements OnInit {

  topCategories$: Observable<TopCategoryListInfo[]>;

  constructor(private categoryService: CategoryService,
    private profileService: ProfileService) { }

  ngOnInit(): void {
    this.profileService.refreshNeeded$.subscribe(() => this.loadCategories());;
    this.loadCategories();
  }

  private loadCategories() {
    this.topCategories$ = this.categoryService.getTopCategories(numberOfCategories)
  }
}
