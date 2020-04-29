import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdvertisementModalEditComponent } from './advertisement-modal-edit.component';

describe('AdvertisementModalEditComponent', () => {
  let component: AdvertisementModalEditComponent;
  let fixture: ComponentFixture<AdvertisementModalEditComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AdvertisementModalEditComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdvertisementModalEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
