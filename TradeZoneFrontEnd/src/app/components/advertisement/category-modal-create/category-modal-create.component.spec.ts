import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CategoryModalCreateComponent } from './category-modal-create.component';

describe('CategoryModalCreateComponent', () => {
  let component: CategoryModalCreateComponent;
  let fixture: ComponentFixture<CategoryModalCreateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CategoryModalCreateComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CategoryModalCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
