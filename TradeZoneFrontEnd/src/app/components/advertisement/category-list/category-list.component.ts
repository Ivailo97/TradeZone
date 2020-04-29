import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { CategoryListInfo } from '../../../core/models/category-info';
import { Observable } from 'rxjs';
import { CategoryService } from '../../../core/services/category.service';
import { TokenStorageService } from 'src/app/core/services/token-storage.service';

const defaultCategoryPhotoUrl = 'https://res.cloudinary.com/knight-cloud/image/upload/v1586525812/via5w3bkaetvx6a5wkgy.png';
const defaultCategoryName = 'All';

@Component({
  selector: 'app-category-list',
  templateUrl: './category-list.component.html',
  styleUrls: ['./category-list.component.css']
})

export class CategoryListComponent implements OnInit {

  roles: string[];

  defaultCategory: CategoryListInfo;

  categories$: Observable<CategoryListInfo[]>;

  @Output() emitter = new EventEmitter<string>();

  constructor(private categoryService: CategoryService,
    private tokenService: TokenStorageService) { }

  ngOnInit() {
    this.roles = this.tokenService.getAuthorities();
    this.defaultCategory = { name: defaultCategoryName, photoUrl: defaultCategoryPhotoUrl };
    this.categoryService.refreshNeeded$.subscribe(() => this.loadCategories())
    this.loadCategories();
  }

  private loadCategories() {
    this.categories$ = this.categoryService.getAllCategories();
  }

  changeCategory(event) {
    this.emitter.emit(event.currentTarget.innerText);
  }
}
