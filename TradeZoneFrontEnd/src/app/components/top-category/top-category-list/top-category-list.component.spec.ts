import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TopCategoryListComponent } from './top-category-list.component';

describe('TopCategoryListComponent', () => {
  let component: TopCategoryListComponent;
  let fixture: ComponentFixture<TopCategoryListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TopCategoryListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TopCategoryListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
