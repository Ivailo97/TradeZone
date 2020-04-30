import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { CategoryListInfo } from '../../../core/models/category-info';


@Component({
  selector: 'app-category-info',
  templateUrl: './category-info.component.html',
  styleUrls: ['./category-info.component.css']
})
export class CategoryInfoComponent implements OnInit {

  @Input() category: CategoryListInfo

  @Output() change = new EventEmitter<string>();

  constructor() { }

  ngOnInit() { }

  sendCategoryToParent(event) {
    event.preventDefault();
    this.setCategoryToActive(event);
    this.change.emit(event)
  }

  private setCategoryToActive(event) {
    for (const li of event.currentTarget.parentNode.parentNode.children) {
      li.children[0].classList.remove('bg-dark');
    }
    event.currentTarget.classList.add('bg-dark');
  }
}
