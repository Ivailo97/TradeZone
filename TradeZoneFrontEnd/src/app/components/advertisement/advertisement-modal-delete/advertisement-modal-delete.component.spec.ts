import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdvertisementModalDeletelComponent } from './advertisement-modal-delete.component';

describe('AdvertisementModalDeletelComponent', () => {
  let component: AdvertisementModalDeletelComponent;
  let fixture: ComponentFixture<AdvertisementModalDeletelComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AdvertisementModalDeletelComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdvertisementModalDeletelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
