import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ImageDeleteModalComponent } from './image-delete-modal.component';

describe('ImageDeleteModalComponent', () => {
  let component: ImageDeleteModalComponent;
  let fixture: ComponentFixture<ImageDeleteModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ImageDeleteModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ImageDeleteModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
