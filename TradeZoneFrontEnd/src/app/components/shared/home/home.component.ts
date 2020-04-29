import { Component, OnInit } from '@angular/core';
import { TokenStorageService } from 'src/app/core/services/token-storage.service';
import { CategoryService } from 'src/app/core/services/category.service';
import { Observable } from 'rxjs';
import { TopCategoryListInfo } from 'src/app/core/models/top-category';

const numberOfCategories = 3;

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  info: any;
  topCategories$: Observable<TopCategoryListInfo[]>;

  constructor(private token: TokenStorageService,
    private categoryService: CategoryService) { }

  ngOnInit() {
    this.info = {
      token: this.token.getToken(),
      username: this.token.getUsername(),
      authorities: this.token.getAuthorities()
    };

    this.topCategories$ = this.categoryService.getTopCategories(numberOfCategories);
  }
}
