import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdvertisementModalCreateComponent } from './advertisement-modal-create.component';

describe('AdvertisementModalCreateComponent', () => {
  let component: AdvertisementModalCreateComponent;
  let fixture: ComponentFixture<AdvertisementModalCreateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AdvertisementModalCreateComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdvertisementModalCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
